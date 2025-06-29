package com.zhukm.sync.config;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new TwoLevelCacheManager(hazelcastInstance);
    }
}
