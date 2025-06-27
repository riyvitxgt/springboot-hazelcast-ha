package com.zhukm.sync.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.zhukm.sync.Constants.*;

@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private final HazelcastInstance hazelcastInstance;

    public CacheService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    /**
     * 获取指定名称的缓存Map
     */
    public <K, V> IMap<K, V> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    /**
     * 存储数据到默认缓存
     */
    public <K, V> void put(K key, V value) {
        IMap<K, V> map = getMap(DEFAULT_CACHE_KEY);
        map.put(key, value);
        logger.debug("数据已存储到默认缓存: key={}", key);
    }

    /**
     * 存储数据到指定缓存，带过期时间
     */
    public <K, V> void put(String mapName, K key, V value, long ttl, TimeUnit timeUnit) {
        IMap<K, V> map = getMap(mapName);
        map.put(key, value, ttl, timeUnit);
        logger.debug("数据已存储到缓存[{}]: key={}, ttl={}s", mapName, key, timeUnit.toSeconds(ttl));
    }

    /**
     * 从默认缓存获取数据
     */
    public <K, V> V get(K key) {
        IMap<K, V> map = getMap(DEFAULT_CACHE_KEY);
        V value = map.get(key);
        logger.debug("从默认缓存获取数据: key={}, found={}", key, value != null);
        return value;
    }

    /**
     * 从指定缓存获取数据
     */
    public <K, V> V get(String mapName, K key) {
        IMap<K, V> map = getMap(mapName);
        V value = map.get(key);
        logger.debug("从缓存[{}]获取数据: key={}, found={}", mapName, key, value != null);
        return value;
    }

    /**
     * 从默认缓存删除数据
     */
    public <K> void remove(K key) {
        IMap<K, Object> map = getMap(DEFAULT_CACHE_KEY);
        map.remove(key);
        logger.debug("从默认缓存删除数据: key={}", key);
    }

    /**
     * 从指定缓存删除数据
     */
    public <K> void remove(String mapName, K key) {
        IMap<K, Object> map = getMap(mapName);
        map.remove(key);
        logger.debug("从缓存[{}]删除数据: key={}", mapName, key);
    }

    /**
     * 检查默认缓存中是否存在指定key
     */
    public <K> boolean containsKey(K key) {
        IMap<K, Object> map = getMap(DEFAULT_CACHE_KEY);
        return map.containsKey(key);
    }

    /**
     * 检查指定缓存中是否存在指定key
     */
    public <K> boolean containsKey(String mapName, K key) {
        IMap<K, Object> map = getMap(mapName);
        return map.containsKey(key);
    }

    /**
     * 获取默认缓存的所有key
     */
    public <K> Set<K> getAllKeys() {
        IMap<K, Object> map = getMap(DEFAULT_CACHE_KEY);
        return map.keySet();
    }

    /**
     * 获取指定缓存的所有key
     */
    public <K> Set<K> getAllKeys(String mapName) {
        IMap<K, Object> map = getMap(mapName);
        return map.keySet();
    }

    /**
     * 获取默认缓存大小
     */
    public int getSize() {
        IMap<Object, Object> map = getMap(DEFAULT_CACHE_KEY);
        return map.size();
    }

    /**
     * 获取指定缓存大小
     */
    public int getSize(String mapName) {
        IMap<Object, Object> map = getMap(mapName);
        return map.size();
    }

    /**
     * 清空默认缓存
     */
    public void clear() {
        IMap<Object, Object> map = getMap(DEFAULT_CACHE_KEY);
        map.clear();
        logger.info("默认缓存已清空");
    }

    /**
     * 清空指定缓存
     */
    public void clear(String mapName) {
        IMap<Object, Object> map = getMap(mapName);
        map.clear();
        logger.info("缓存[{}]已清空", mapName);
    }

    /**
     * 用户缓存操作
     */
    public void putUser(String userId, Object userInfo) {
        put(USER_CACHE_KEY, userId, userInfo, 2, TimeUnit.HOURS);
    }

    public <T> T getUser(String userId) {
        return get(USER_CACHE_KEY, userId);
    }

    public void removeUser(String userId) {
        remove(USER_CACHE_KEY, userId);
    }

    /**
     * 会话缓存操作
     */
    public void putSession(String sessionId, Object sessionInfo) {
        put(SESSION_CACHE_KEY, sessionId, sessionInfo, 30, TimeUnit.MINUTES);
    }

    public <T> T getSession(String sessionId) {
        return get(SESSION_CACHE_KEY, sessionId);
    }

    public void removeSession(String sessionId) {
        remove(SESSION_CACHE_KEY, sessionId);
    }
}
