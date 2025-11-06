package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存测试接口
 * 
 * 测试RedisCache工具类的各种操作
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test/redis")
public class RedisCacheTestController {

    @Autowired
    private RedisCache redisCache;

    // =============================String操作测试=============================

    /**
     * 测试1：String基本操作
     * 
     * GET /test/redis/string
     */
    @GetMapping("/string")
    public AjaxResult testString() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 设置缓存
        redisCache.set("test:string:name", "张三");
        redisCache.set("test:string:age", 25);
        
        // 2. 获取缓存
        String name = redisCache.get("test:string:name");
        Integer age = redisCache.get("test:string:age");
        result.put("name", name);
        result.put("age", age);
        
        // 3. 设置带过期时间的缓存（10秒）
        redisCache.set("test:string:temp", "临时数据", 10);
        long expire = redisCache.getExpire("test:string:temp");
        result.put("tempExpire", expire + "秒");
        
        // 4. 判断key是否存在
        boolean hasName = redisCache.hasKey("test:string:name");
        result.put("hasName", hasName);
        
        // 5. 递增/递减操作
        redisCache.set("test:string:counter", 0);
        long counter = redisCache.increment("test:string:counter", 5);
        result.put("counter", counter);
        
        return AjaxResult.success("String操作测试成功", result);
    }

    /**
     * 测试2：String删除操作
     * 
     * DELETE /test/redis/string
     */
    @DeleteMapping("/string")
    public AjaxResult deleteString() {
        boolean deleted = redisCache.delete("test:string:name");
        return AjaxResult.success("删除成功", deleted);
    }

    // =============================List操作测试=============================

    /**
     * 测试3：List操作
     * 
     * GET /test/redis/list
     */
    @GetMapping("/list")
    public AjaxResult testList() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 添加单个元素
        redisCache.listRightPush("test:list:users", "用户1");
        redisCache.listRightPush("test:list:users", "用户2");
        redisCache.listRightPush("test:list:users", "用户3");
        
        // 2. 批量添加
        List<String> newUsers = Arrays.asList("用户4", "用户5");
        redisCache.listRightPushAll("test:list:users", newUsers);
        
        // 3. 获取列表
        List<String> users = redisCache.getList("test:list:users");
        result.put("users", users);
        
        // 4. 获取列表长度
        long size = redisCache.getListSize("test:list:users");
        result.put("size", size);
        
        // 5. 修改列表元素
        redisCache.listSet("test:list:users", 0, "管理员");
        List<String> updatedUsers = redisCache.getList("test:list:users");
        result.put("updatedUsers", updatedUsers);
        
        return AjaxResult.success("List操作测试成功", result);
    }

    // =============================Hash操作测试=============================

    /**
     * 测试4：Hash操作
     * 
     * GET /test/redis/hash
     */
    @GetMapping("/hash")
    public AjaxResult testHash() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 设置Hash数据
        redisCache.hashPut("test:hash:user:1", "name", "张三");
        redisCache.hashPut("test:hash:user:1", "age", 25);
        redisCache.hashPut("test:hash:user:1", "email", "zhangsan@hrone.com");
        
        // 2. 获取Hash数据
        String name = redisCache.hashGet("test:hash:user:1", "name");
        Integer age = redisCache.hashGet("test:hash:user:1", "age");
        result.put("name", name);
        result.put("age", age);
        
        // 3. 批量设置Hash数据
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "李四");
        user2.put("age", 30);
        user2.put("email", "lisi@hrone.com");
        redisCache.hashPutAll("test:hash:user:2", user2);
        
        // 4. 获取所有Hash数据
        Map<Object, Object> allUser2 = redisCache.hashGetAll("test:hash:user:2");
        result.put("user2", allUser2);
        
        // 5. 获取所有键
        Set<Object> keys = redisCache.hashKeys("test:hash:user:1");
        result.put("keys", keys);
        
        // 6. 判断Hash键是否存在
        boolean hasName = redisCache.hashHasKey("test:hash:user:1", "name");
        result.put("hasName", hasName);
        
        return AjaxResult.success("Hash操作测试成功", result);
    }

    // =============================Set操作测试=============================

    /**
     * 测试5：Set操作
     * 
     * GET /test/redis/set
     */
    @GetMapping("/set")
    public AjaxResult testSet() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 添加元素
        redisCache.setAdd("test:set:tags", "Java", "Python", "Go");
        redisCache.setAdd("test:set:tags", "Java"); // 重复元素不会添加
        
        // 2. 获取所有元素
        Set<String> tags = redisCache.getSet("test:set:tags");
        result.put("tags", tags);
        
        // 3. 获取Set大小
        long size = redisCache.getSetSize("test:set:tags");
        result.put("size", size);
        
        // 4. 判断元素是否存在
        boolean hasJava = redisCache.setHasValue("test:set:tags", "Java");
        boolean hasCpp = redisCache.setHasValue("test:set:tags", "C++");
        result.put("hasJava", hasJava);
        result.put("hasCpp", hasCpp);
        
        // 5. 移除元素
        long removed = redisCache.setRemove("test:set:tags", "Python");
        Set<String> afterRemove = redisCache.getSet("test:set:tags");
        result.put("removed", removed);
        result.put("afterRemove", afterRemove);
        
        return AjaxResult.success("Set操作测试成功", result);
    }

    // =============================过期时间测试=============================

    /**
     * 测试6：过期时间操作
     * 
     * GET /test/redis/expire
     */
    @GetMapping("/expire")
    public AjaxResult testExpire() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 设置缓存
        redisCache.set("test:expire:data", "测试数据");
        
        // 2. 设置过期时间（60秒）
        redisCache.expire("test:expire:data", 60);
        long expire = redisCache.getExpire("test:expire:data");
        result.put("expire", expire + "秒");
        
        // 3. 设置过期时间（分钟）
        redisCache.set("test:expire:data2", "测试数据2");
        redisCache.expire("test:expire:data2", 5, TimeUnit.MINUTES);
        long expire2 = redisCache.getExpire("test:expire:data2");
        result.put("expire2", expire2 + "秒");
        
        // 4. 直接设置带过期时间的缓存
        redisCache.set("test:expire:data3", "测试数据3", 30);
        long expire3 = redisCache.getExpire("test:expire:data3");
        result.put("expire3", expire3 + "秒");
        
        return AjaxResult.success("过期时间测试成功", result);
    }

    // =============================模糊查询测试=============================

    /**
     * 测试7：模糊查询
     * 
     * GET /test/redis/keys
     */
    @GetMapping("/keys")
    public AjaxResult testKeys() {
        // 1. 查询所有test:string开头的key
        Set<String> stringKeys = redisCache.keys("test:string:*");
        
        // 2. 查询所有test:hash开头的key
        Set<String> hashKeys = redisCache.keys("test:hash:*");
        
        // 3. 查询所有test开头的key
        Set<String> allKeys = redisCache.keys("test:*");
        
        Map<String, Object> result = new HashMap<>();
        result.put("stringKeys", stringKeys);
        result.put("hashKeys", hashKeys);
        result.put("allKeys", allKeys);
        result.put("totalCount", allKeys.size());
        
        return AjaxResult.success("模糊查询测试成功", result);
    }

    // =============================批量删除测试=============================

    /**
     * 测试8：批量删除
     * 
     * DELETE /test/redis/batch
     */
    @DeleteMapping("/batch")
    public AjaxResult deleteBatch() {
        // 删除所有test:string开头的key
        long count = redisCache.deleteKeys("test:string:*");
        return AjaxResult.success("批量删除成功，共删除" + count + "个key");
    }

    // =============================清理测试数据=============================

    /**
     * 测试9：清理所有测试数据
     * 
     * DELETE /test/redis/clean
     */
    @DeleteMapping("/clean")
    public AjaxResult cleanAll() {
        // 删除所有test开头的key
        long count = redisCache.deleteKeys("test:*");
        return AjaxResult.success("清理完成，共删除" + count + "个key");
    }

    // =============================对象缓存测试=============================

    /**
     * 测试10：对象缓存
     * 
     * GET /test/redis/object
     */
    @GetMapping("/object")
    public AjaxResult testObject() {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 缓存对象
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setName("张三");
        user.setAge(25);
        user.setEmail("zhangsan@hrone.com");
        
        redisCache.set("test:object:user:1", user, 60);
        
        // 2. 获取对象
        UserInfo cachedUser = redisCache.get("test:object:user:1");
        result.put("user", cachedUser);
        
        // 3. 缓存列表
        List<UserInfo> users = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            UserInfo u = new UserInfo();
            u.setId((long) i);
            u.setName("用户" + i);
            u.setAge(20 + i);
            u.setEmail("user" + i + "@hrone.com");
            users.add(u);
        }
        redisCache.set("test:object:users", users, 60);
        
        // 4. 获取列表
        List<UserInfo> cachedUsers = redisCache.get("test:object:users");
        result.put("users", cachedUsers);
        
        return AjaxResult.success("对象缓存测试成功", result);
    }

    /**
     * 用户信息测试类
     */
    static class UserInfo {
        private Long id;
        private String name;
        private Integer age;
        private String email;

        // Getter and Setter
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

