package com.zhukm.sync.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
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
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setInstanceName("two-level-cache-instance");

        // 配置Map（二级缓存）
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("default")
                .setTimeToLiveSeconds(1800) // 30分钟TTL
                .setMaxIdleSeconds(600);    // 10分钟空闲过期

        config.addMapConfig(mapConfig);

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    @Primary
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new TwoLevelCacheManager(hazelcastInstance);
    }
}
