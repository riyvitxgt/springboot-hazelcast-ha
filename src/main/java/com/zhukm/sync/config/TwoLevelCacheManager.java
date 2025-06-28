package com.zhukm.sync.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TwoLevelCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private final HazelcastInstance hazelcastInstance;

    public TwoLevelCacheManager(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    @Nullable
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, cacheName -> {
            IMap<Object, Object> l2Cache = hazelcastInstance.getMap(cacheName);
            return new TwoLevelCache(cacheName, l2Cache);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}
