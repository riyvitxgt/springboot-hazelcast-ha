package com.zhukm.sync.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hazelcast.map.IMap;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TwoLevelCache extends AbstractValueAdaptingCache {

    private final String name;
    private final Cache<Object, Object> l1Cache; // Caffeine一级缓存
    private final IMap<Object, Object> l2Cache;  // Hazelcast二级缓存

    public TwoLevelCache(String name, IMap<Object, Object> l2Cache) {
        super(true); // 允许null值
        this.name = name;
        this.l2Cache = l2Cache;
        // 配置Caffeine一级缓存
        this.l1Cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.l1Cache;
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        // 先从一级缓存查找
        Object value = l1Cache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        // 一级缓存未命中，从二级缓存查找
        value = l2Cache.get(key);
        if (value != null) {
            // 将二级缓存的数据放入一级缓存
            l1Cache.put(key, value);
        }

        return value;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        // 同时放入一级和二级缓存
        l1Cache.put(key, value);
        l2Cache.put(key, value);
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object existingValue = lookup(key);
        if (existingValue == null) {
            put(key, value);
            return null;
        }
        return toValueWrapper(existingValue);
    }

    @Override
    public void evict(Object key) {
        // 从两级缓存中都删除
        l1Cache.invalidate(key);
        l2Cache.remove(key);
    }

    @Override
    public void clear() {
        // 清空两级缓存
        l1Cache.invalidateAll();
        l2Cache.clear();
    }

    @Override
    @Nullable
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (value != null) {
            return (T) fromStoreValue(value);
        }

        try {
            T loadedValue = valueLoader.call();
            put(key, toStoreValue(loadedValue));
            return loadedValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

