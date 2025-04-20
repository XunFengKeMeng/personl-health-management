package com.example.health.cache;

import com.example.health.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author huanghaiming
 * @date 2025/4/20 21:28
 * @description: 基于Redis的MyBatis二级缓存实现类
 */
@Slf4j
public class RedisCache implements Cache {
    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    // Redis操作模板，延迟初始化
    private RedisTemplate redisTemplate;
    // 缓存标识，作为 Redis Hash 的 key
    private final String id;

    /**
     * 首次使用时从 Spring 容器获取 RedisTemplate（延迟初始化）
     */
    private RedisTemplate getRedisTemplate(){
        //通过ApplicationContextHolder工具类获取RedisTemplate
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate) ApplicationContextHolder.getBeanByName("redisTemplate");
        }
        return redisTemplate;
    }

    public RedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 存储数据
     * 如果要实现序列化，记得处理value的序列化问题，否则会导致存储异常
     */
    @Override
    public void putObject(Object key, Object value) {
        //使用redis的Hash类型进行存储
        getRedisTemplate().opsForHash().put(id,key.toString(),value);
    }

    /**
     * 获取数据
     */
    @Override
    public Object getObject(Object key) {
        try {
            //根据key从redis中获取数据
            return getRedisTemplate().opsForHash().get(id,key.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("缓存出错 ");
        }
        return null;
    }

    /**
     * 直接删除key
     */
    @Override
    public Object removeObject(Object key) {
        if (key != null) {
            getRedisTemplate().delete(key.toString());
        }
        return null;
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        log.debug("清空缓存");
        Set<String> keys = getRedisTemplate().keys("*:" + this.id + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            getRedisTemplate().delete(keys);
        }
    }

    /**
     * 返回整个Redis的key的数量，而不是当前缓存的 size
     */
    @Override
    public int getSize() {
        Long size = (Long) getRedisTemplate().execute((RedisCallback<Long>) RedisServerCommands::dbSize);
        return size.intValue();
    }

    /**
     * 返回读写实例
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
