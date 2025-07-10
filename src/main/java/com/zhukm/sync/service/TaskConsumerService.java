package com.zhukm.sync.service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceNotActiveException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zhukm.sync.config.Constants.TASK_QUEUE;

@Service
public class TaskConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(TaskConsumerService.class);

    private final HazelcastInstance hazelcastInstance;
    private final ExecutorService taskExecutor;


    public TaskConsumerService(HazelcastInstance hazelcastInstance, ExecutorService taskExecutor) {
        this.hazelcastInstance = hazelcastInstance;
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void init() {
        // 启动多个消费者线程
        for (int i = 0; i < 4; i++) {
            taskExecutor.submit(this::consumeTasks);
        }
    }

    private void consumeTasks() {
        IQueue<String> queue = hazelcastInstance.getQueue(TASK_QUEUE);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 阻塞获取任务
                String task = queue.take();
                processTask(task);
            } catch (InterruptedException e) {
                logger.info("Consumer thread interrupted, shutting down");
                Thread.currentThread().interrupt();
                break; // 退出循环
            } catch (HazelcastInstanceNotActiveException e) {
                logger.warn("Hazelcast instance is not active, stopping consumer");
                break; // Hazelcast 实例关闭时退出
            } catch (Exception e) {
                logger.error("Unexpected error while consuming task", e);
                try {
                    Thread.sleep(1000); // 防止快速重试
                } catch (InterruptedException ie) {
                    logger.info("Consumer thread interrupted during sleep, shutting down");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void processTask(String task) {
        // 模拟任务处理
        logger.info("Processing task: {} on node: {}", task, hazelcastInstance.getCluster().getLocalMember());
        try {
            Thread.sleep(1000); // 模拟耗时操作
        } catch (InterruptedException e) {
            logger.warn("Task processing interrupted, continuing with next task", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        // 在应用关闭时终止线程池
        logger.info("Shutting down TaskConsumer, stopping executor service");
        taskExecutor.shutdown();
        try {
            if (!taskExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warn("Executor did not terminate in time, forcing shutdown");
                taskExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted during executor shutdown", e);
            taskExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
