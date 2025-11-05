# 第2阶段：通用工具模块

## 🎯 学习目标

- 掌握统一响应结果的封装
- 实现常用工具类
- 理解异常处理体系
- 学习常量的组织方式

## 📋 任务清单

- [ ] 2.1 统一响应结果（1小时）
- [ ] 2.2 基础工具类（2小时）
- [ ] 2.3 通用实体类（1小时）
- [ ] 2.4 异常处理（2小时）
- [ ] 2.5 常量定义（30分钟）

## 🏗️ 模块说明

在 `hrone-common` 模块中实现所有通用工具类，这个模块会被其他所有模块依赖。

---

## 📝 2.1 统一响应结果

### 为什么需要统一响应格式？

在实际项目中，前后端需要约定统一的数据格式：

**好处：**
- 前端可以统一处理响应
- 便于错误处理
- 提高代码可维护性

**标准响应格式：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { ... }
}
```

### 实现步骤

#### 步骤1：创建HTTP状态码常量

创建文件：`hrone-common/src/main/java/com/hrone/common/constant/HttpStatus.java`

```java
package com.hrone.common.constant;

/**
 * HTTP状态码常量
 * 
 * @author hrone
 */
public class HttpStatus {
    
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;
    
    /**
     * 系统警告消息
     */
    public static final int WARN = 601;
}
```

**知识点：**
- 使用接口常量代替魔法值
- HTTP标准状态码的含义
- 自定义业务状态码（如601）

#### 步骤2：创建统一响应类

创建文件：`hrone-common/src/main/java/com/hrone/common/core/domain/AjaxResult.java`

```java
package com.hrone.common.core.domain;

import java.util.HashMap;

/**
 * 操作消息提醒
 * 
 * @author hrone
 */
public class AjaxResult extends HashMap<String, Object> {
    
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @return 错误消息
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 错误消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 方便链式调用
     * 
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
```

**知识点：**
- 继承HashMap实现灵活的数据结构
- 使用静态工厂方法简化对象创建
- 链式调用（Fluent API）
- 方法重载提供多种使用方式

#### 步骤3：修改TestController测试

修改文件：`hrone-admin/src/main/java/com/hrone/controller/TestController.java`

```java
package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试Controller
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    /**
     * 测试接口 - 使用AjaxResult
     */
    @GetMapping("/hello")
    public AjaxResult hello() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "项目启动成功！");
        data.put("time", LocalDateTime.now());
        data.put("version", "1.0.0");
        
        return AjaxResult.success("Hello, HROne!", data);
    }
    
    /**
     * 测试成功响应
     */
    @GetMapping("/success")
    public AjaxResult testSuccess() {
        return AjaxResult.success("操作成功");
    }
    
    /**
     * 测试成功响应带数据
     */
    @GetMapping("/success-data")
    public AjaxResult testSuccessData() {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1);
        user.put("name", "张三");
        user.put("age", 25);
        
        return AjaxResult.success("查询成功", user);
    }
    
    /**
     * 测试错误响应
     */
    @GetMapping("/error")
    public AjaxResult testError() {
        return AjaxResult.error("操作失败");
    }
    
    /**
     * 测试自定义状态码
     */
    @GetMapping("/custom")
    public AjaxResult testCustom() {
        return AjaxResult.error(601, "自定义警告消息");
    }
    
    /**
     * 测试链式调用
     */
    @GetMapping("/chain")
    public AjaxResult testChain() {
        return AjaxResult.success()
                .put("page", 1)
                .put("size", 10)
                .put("total", 100);
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public AjaxResult health() {
        return AjaxResult.success("服务运行正常")
                .put("status", "UP")
                .put("timestamp", System.currentTimeMillis());
    }
}
```

---

### 测试统一响应结果

**1. 启动项目**

在IDEA中运行 `HROneApplication`

**2. 测试接口**

```bash
# 测试成功响应
http://localhost:8080/test/success

# 响应示例
{
  "code": 200,
  "msg": "操作成功"
}

# 测试成功响应带数据
http://localhost:8080/test/success-data

# 响应示例
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "id": 1,
    "name": "张三",
    "age": 25
  }
}

# 测试错误响应
http://localhost:8080/test/error

# 响应示例
{
  "code": 500,
  "msg": "操作失败"
}

# 测试链式调用
http://localhost:8080/test/chain

# 响应示例
{
  "code": 200,
  "msg": "操作成功",
  "page": 1,
  "size": 10,
  "total": 100
}
```

---

## ✅ 2.1 完成检查

- [ ] HttpStatus常量类创建成功
- [ ] AjaxResult响应类创建成功
- [ ] 测试Controller已更新
- [ ] 所有测试接口响应正常
- [ ] 理解了统一响应的好处

---

## 💡 知识点总结

### 1. 为什么要统一响应格式？

**前端的好处：**
- 可以统一处理响应数据
- 统一的错误提示
- 便于拦截器处理

**后端的好处：**
- 代码更规范
- 便于维护
- 提高开发效率

### 2. AjaxResult设计要点

**继承HashMap的原因：**
- 灵活性高，可以添加任意字段
- 自动序列化为JSON
- 支持链式调用

**静态工厂方法的好处：**
- 方法名更有意义（success、error）
- 提供多种重载版本
- 简化对象创建

### 3. 实际应用场景

```java
// 场景1：简单成功
return AjaxResult.success();

// 场景2：成功并返回数据
return AjaxResult.success(user);

// 场景3：失败并说明原因
return AjaxResult.error("用户名已存在");

// 场景4：自定义状态码
return AjaxResult.error(601, "需要人工审核");

// 场景5：链式添加字段
return AjaxResult.success()
    .put("token", "xxx")
    .put("expireTime", 3600);
```

---

## 📚 扩展学习

1. **HTTP状态码**
   - 1xx：信息性状态码
   - 2xx：成功状态码
   - 3xx：重定向状态码
   - 4xx：客户端错误
   - 5xx：服务器错误

2. **RESTful API设计规范**
   - 资源命名
   - HTTP方法使用
   - 状态码选择
   - 错误处理

3. **其他响应格式设计**
   - Google JSON Style Guide
   - JSend规范
   - JSON:API规范

---

**下一步：** 继续实现 2.2 基础工具类

阅读详细教程：继续往下看本文档

