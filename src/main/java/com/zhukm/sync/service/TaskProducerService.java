package com.zhukm.sync.service;

import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.zhukm.sync.config.Constants.*;

@Service
public class TaskProducerService {
    private static final Logger logger = LoggerFactory.getLogger(TaskProducerService.class);

    private final HazelcastInstance hazelcastInstance;

    private String nodeId; // 当前节点唯一标识
    private IMap<String, LeaderInfo> leaderMap;

    public TaskProducerService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }


    // 存储领导者信息的类
    record LeaderInfo(String nodeId, long timestamp) {
    }

    @PostConstruct
    public void init() {
        // 初始化节点 ID 和 IMap
        nodeId = hazelcastInstance.getCluster().getLocalMember().getUuid().toString();
        leaderMap = hazelcastInstance.getMap(LEADER_ELECTION_MAP);
        hazelcastInstance.getCluster().addMembershipListener(new MembershipListener() {
            @Override
            public void memberAdded(MembershipEvent membershipEvent) {
                logger.info("Member added: {}", membershipEvent.getMember());
                tryBecomeLeader(); // 新节点加入时尝试选举
            }

            @Override
            public void memberRemoved(MembershipEvent membershipEvent) {
                logger.info("Member removed: {}", membershipEvent.getMember());
                LeaderInfo currentLeader = leaderMap.get(LEADER_ELECTION_KEY);
                // 如果有节点下线，且下线的是主节点，尝试成为新的主节点
                if (currentLeader != null && currentLeader.nodeId().equals(membershipEvent.getMember().getUuid().toString())) {
                    logger.info("Leader node {} went down, triggering leader election", currentLeader.nodeId());
                    tryBecomeLeader();
                }
            }
        });
    }

    @Scheduled(fixedRate = 10_000) // 每 10 秒触发任务生产
    public void produceTasks() {
        try {
            if (tryBecomeLeader()) {
                IQueue<String> queue = hazelcastInstance.getQueue("task-queue");
                for (int i = 0; i < 10; i++) {
                    String task = "Task-" + UUID.randomUUID();
                    boolean result = queue.offer(task);
                    logger.info("Produced task: {} {} by node: {}", task, result, nodeId);
                }
            } else {
                logger.debug("Node {} is not the leader, skipping task production", nodeId);
            }
        } catch (Exception e) {
            logger.error("Error producing task", e);
        }
    }

    @Scheduled(fixedRate = 5_000)
    public void checkLeadership() {
        tryBecomeLeader();
    }

    private boolean tryBecomeLeader() {
        // 统一检查 quorum
        if (!hasQuorum()) {
            logger.warn("Cluster size {} is below quorum ({}), cannot become leader",
                    hazelcastInstance.getCluster().getMembers().size(), MIN_CLUSTER_SIZE);
            return false;
        }

        try {
            LeaderInfo newLeader = new LeaderInfo(nodeId, System.currentTimeMillis());
            LeaderInfo currentLeader = leaderMap.get(LEADER_ELECTION_KEY);
            logger.debug("Current leader: {}", currentLeader == null ? "none" : currentLeader.nodeId());

            long currentTime = System.currentTimeMillis();
            if (currentLeader == null) {
                if (leaderMap.putIfAbsent(LEADER_ELECTION_KEY, newLeader, LEASE_TIME_SECONDS, TimeUnit.SECONDS) == null) {
                    logger.info("Node {} became the leader (no previous leader)", nodeId);
                    return true;
                }
            } else if ((currentTime - currentLeader.timestamp()) > LEASE_TIME_SECONDS * 1000) {
                if (leaderMap.replace(LEADER_ELECTION_KEY, currentLeader, newLeader)) {
                    leaderMap.set(LEADER_ELECTION_KEY, newLeader, LEASE_TIME_SECONDS, TimeUnit.SECONDS);
                    logger.info("Node {} took over as leader due to lease expiration", nodeId);
                    return true;
                }
            } else if (currentLeader.nodeId().equals(nodeId)) {
                leaderMap.set(LEADER_ELECTION_KEY, newLeader, LEASE_TIME_SECONDS, TimeUnit.SECONDS);
                logger.debug("Node {} extended lease as leader", nodeId);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error during leader election", e);
            return false;
        }
    }

    /**
     * 判断集群中节点是否超过2个，只有超过2个才允许选举成为主节点，避免脑裂
     *
     * @return true:节点数大于等于2;false:节点数小于2
     */
    private boolean hasQuorum() {
        return hazelcastInstance.getCluster().getMembers().size() >= MIN_CLUSTER_SIZE;
    }
}

