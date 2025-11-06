package com.hrone.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 * 
 * 提供常用的缓存操作方法
 * 
 * @author hrone
 */
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // =============================String操作=============================

    /**
     * 设置缓存
     * 
     * @param key 键
     * @param value 值
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存，并指定过期时间
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间（秒）
     */
    public <T> void set(String key, T value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存，并指定过期时间和时间单位
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public <T> void set(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取缓存
     * 
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 批量删除缓存
     * 
     * @param keys 键集合
     * @return 删除的数量
     */
    public long delete(Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count == null ? 0 : count;
    }

    /**
     * 判断key是否存在
     * 
     * @param key 键
     * @return true=存在，false=不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置过期时间
     * 
     * @param key 键
     * @param timeout 过期时间（秒）
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * 设置过期时间，并指定时间单位
     * 
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取过期时间
     * 
     * @param key 键
     * @return 过期时间（秒），-1=永不过期，-2=不存在
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? -2 : expire;
    }

    /**
     * 递增
     * 
     * @param key 键
     * @param delta 递增值
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result == null ? 0 : result;
    }

    /**
     * 递减
     * 
     * @param key 键
     * @param delta 递减值
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        Long result = redisTemplate.opsForValue().decrement(key, delta);
        return result == null ? 0 : result;
    }

    // =============================List操作=============================

    /**
     * 获取list缓存的内容
     * 
     * @param key 键
     * @param start 开始位置（0开始）
     * @param end 结束位置（-1表示全部）
     * @return 列表
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, long start, long end) {
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的所有内容
     * 
     * @param key 键
     * @return 列表
     */
    public <T> List<T> getList(String key) {
        return getList(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     * 
     * @param key 键
     * @return 长度
     */
    public long getListSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 将值放入list缓存
     * 
     * @param key 键
     * @param value 值
     * @return 是否添加成功
     */
    public <T> boolean listRightPush(String key, T value) {
        redisTemplate.opsForList().rightPush(key, value);
        return true;
    }

    /**
     * 将值放入list缓存，并设置过期时间
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间（秒）
     * @return 是否添加成功
     */
    public <T> boolean listRightPush(String key, T value, long timeout) {
        redisTemplate.opsForList().rightPush(key, value);
        return expire(key, timeout);
    }

    /**
     * 将多个值放入list缓存
     * 
     * @param key 键
     * @param values 值列表
     * @return 是否添加成功
     */
    public <T> boolean listRightPushAll(String key, List<T> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
        return true;
    }

    /**
     * 根据索引修改list中的某条数据
     * 
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return 是否修改成功
     */
    public <T> boolean listSet(String key, long index, T value) {
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    /**
     * 移除list中的N个值为value的元素
     * 
     * @param key 键
     * @param count 移除数量
     * @param value 值
     * @return 移除的个数
     */
    public long listRemove(String key, long count, Object value) {
        Long removed = redisTemplate.opsForList().remove(key, count, value);
        return removed == null ? 0 : removed;
    }

    // =============================Hash操作=============================

    /**
     * 获取Hash中的数据
     * 
     * @param key 键
     * @param hashKey Hash键
     * @return Hash值
     */
    @SuppressWarnings("unchecked")
    public <T> T hashGet(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 设置Hash中的数据
     * 
     * @param key 键
     * @param hashKey Hash键
     * @param value 值
     * @return 是否设置成功
     */
    public <T> boolean hashPut(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return true;
    }

    /**
     * 批量设置Hash数据
     * 
     * @param key 键
     * @param map 多个键值对
     * @return 是否设置成功
     */
    public boolean hashPutAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
        return true;
    }

    /**
     * 删除Hash中的数据
     * 
     * @param key 键
     * @param hashKeys Hash键（可以多个）
     * @return 删除的数量
     */
    public long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断Hash中是否有该项的值
     * 
     * @param key 键
     * @param hashKey Hash键
     * @return true=存在，false=不存在
     */
    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取Hash中所有键值对
     * 
     * @param key 键
     * @return 所有键值对
     */
    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取Hash中所有的键
     * 
     * @param key 键
     * @return 所有的键
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取Hash中所有的值
     * 
     * @param key 键
     * @return 所有的值
     */
    public List<Object> hashValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    // =============================Set操作=============================

    /**
     * 根据key获取Set中的所有值
     * 
     * @param key 键
     * @return Set集合
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSet(String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从Set中查询，是否存在
     * 
     * @param key 键
     * @param value 值
     * @return true=存在，false=不存在
     */
    public boolean setHasValue(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 将数据放入Set缓存
     * 
     * @param key 键
     * @param values 值（可以是多个）
     * @return 成功个数
     */
    public long setAdd(String key, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        return count == null ? 0 : count;
    }

    /**
     * 将数据放入Set缓存，并设置过期时间
     * 
     * @param key 键
     * @param timeout 过期时间（秒）
     * @param values 值（可以是多个）
     * @return 成功个数
     */
    public long setAdd(String key, long timeout, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, timeout);
        return count == null ? 0 : count;
    }

    /**
     * 获取Set缓存的大小
     * 
     * @param key 键
     * @return 大小
     */
    public long getSetSize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 移除Set中的值
     * 
     * @param key 键
     * @param values 值（可以是多个）
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count == null ? 0 : count;
    }

    // =============================通用操作=============================

    /**
     * 获取所有符合pattern的key
     * 
     * @param pattern 模式（如：user:*）
     * @return key集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除所有符合pattern的key
     * 
     * @param pattern 模式（如：user:*）
     * @return 删除的数量
     */
    public long deleteKeys(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        return delete(keys);
    }
}

