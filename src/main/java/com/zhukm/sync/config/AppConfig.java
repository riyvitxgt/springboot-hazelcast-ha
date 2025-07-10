package com.zhukm.sync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {
    @Bean
    public ExecutorService taskExecutor() {
        // 创建线程池用于任务消费
        return Executors.newFixedThreadPool(4);
    }
}
