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

---

## 📝 2.2 基础工具类

### 已完成 ✅

**实现的工具类：**
1. **StringUtils.java** - 字符串工具类（388行）
   - 继承 Apache Commons Lang3 的 StringUtils
   - 添加 format() 占位符替换方法
   - 添加 toUnderScoreCase() 驼峰转下划线
   - 添加 join() 集合拼接方法
   - 支持集合、Map、数组的判空

2. **DateUtils.java** - 日期工具类（436行）
   - 使用 Java 8 的 LocalDateTime API
   - 提供 Date 与 LocalDateTime 互转
   - 统一日期格式常量
   - 提供日期计算方法

3. **ServletUtils.java** - Servlet工具类（343行）
   - 使用 RequestContextHolder 获取上下文
   - 提供Request/Response获取方法
   - 提供参数获取和类型转换
   - 提供客户端IP获取（支持代理）

详细使用说明请查看：`第2阶段-快速测试.md`

---

## 📝 2.3 通用实体类

### 已完成 ✅

**实现的类：**
1. **BaseEntity.java** - 基础实体类（174行）
   - 所有实体类的父类
   - 包含通用字段：createBy、createTime、updateBy、updateTime、remark
   - 使用 @JsonFormat 统一日期格式
   - 实现 Serializable 支持序列化

2. **PageDomain.java** - 分页参数（144行）
   - pageNum：当前页码
   - pageSize：每页大小
   - orderByColumn：排序字段
   - isAsc：排序方式

3. **TableDataInfo.java** - 分页响应（122行）
   - total：总记录数
   - rows：数据列表
   - code、msg：状态信息

4. **BaseController.java** - Controller基类（103行）
   - 提供 getDataTable() 方法
   - 提供 toAjax() 方法
   - 所有Controller继承此类

5. **PageTestController.java** - 分页测试接口（208行）
   - 5个分页测试场景
   - 演示 BaseEntity 的使用

详细使用说明请查看：`第2.3阶段-通用实体类测试.md`

---

## 📝 2.4 异常处理

### 已完成 ✅

**实现的类：**
1. **BaseException.java** - 基础异常类（185行）
   - 所有自定义异常的父类
   - 支持模块、错误码、参数等扩展信息
   - 继承 RuntimeException

2. **ServiceException.java** - 业务异常（149行）
   - 用于业务逻辑中的可预见异常
   - 支持错误码和详细错误信息
   - 简化异常抛出

3. **GlobalExceptionHandler.java** - 全局异常处理器（173行）
   - 使用 @RestControllerAdvice 注解
   - 捕获所有Controller抛出的异常
   - 统一转换为 AjaxResult 响应
   - 记录异常日志

4. **ExceptionTestController.java** - 异常测试接口（201行）
   - 8种异常场景测试
   - 演示业务异常处理
   - 演示系统异常处理

### 异常处理架构

```java
// Controller层 - 不需要try-catch
@GetMapping("/{id}")
public AjaxResult getUser(@PathVariable Long id) {
    User user = userService.getUserById(id);
    if (user == null) {
        // 直接抛出异常
        throw new ServiceException("用户不存在", 404);
    }
    return AjaxResult.success(user);
}

// GlobalExceptionHandler - 统一捕获
@ExceptionHandler(ServiceException.class)
public AjaxResult handleServiceException(ServiceException e) {
    log.error("业务异常：{}", e.getMessage());
    return AjaxResult.error(e.getCode(), e.getMessage());
}
```

### 使用示例

**Service层抛出异常：**
```java
@Service
public class UserServiceImpl {
    
    public User getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new ServiceException("用户ID不能为空", 400);
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException("用户不存在", 404);
        }
        
        return user;
    }
}
```

**Controller层不需要处理：**
```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/{id}")
    public AjaxResult getUser(@PathVariable Long id) {
        // 异常会自动被 GlobalExceptionHandler 捕获
        User user = userService.getUserById(id);
        return AjaxResult.success(user);
    }
}
```

详细使用说明请查看：`第2.4阶段-异常处理测试.md`

---

## 📊 第2阶段总结

### 已完成内容（4/5）

| 子阶段 | 状态 | 文件数 | 代码行数 | 时间 |
|-------|------|--------|---------|------|
| 2.1 统一响应结果 | ✅ | 2 | 449 | 1小时 |
| 2.2 基础工具类 | ✅ | 4 | 1,356 | 2小时 |
| 2.3 通用实体类 | ✅ | 5 | 751 | 1小时 |
| 2.4 异常处理 | ✅ | 4 | 681 | 2小时 |
| 2.5 常量定义 | ⏳ | - | - | 0.5小时 |
| **合计** | **80%** | **15** | **3,237** | **6.5/7** |

### 测试接口统计

- 统一响应：8个接口
- 工具类：5个接口
- 分页：5个接口
- 异常处理：8个接口
- **合计：26个测试接口**

---

## 🎯 下一步

### 2.5 常量定义（预计30分钟）

### 已完成 ✅

**实现的类：**
1. **Constants.java** - 通用常量（158行）
   - 字符集常量（UTF8、GBK）
   - 协议常量（HTTP、HTTPS）
   - 状态码常量（SUCCESS、FAIL）
   - Token相关常量
   - 验证码配置常量
   - 40+个通用常量

2. **UserConstants.java** - 用户常量（137行）
   - 用户状态常量（USER_NORMAL、USER_DISABLE）
   - 角色状态常量（ROLE_NORMAL、ROLE_DISABLE）
   - 菜单类型常量（TYPE_DIR、TYPE_MENU、TYPE_BUTTON）
   - 长度限制常量（用户名、密码长度）
   - 超级管理员ID（ADMIN_ID）
   - 30+个用户相关常量

3. **CacheConstants.java** - 缓存常量（123行）
   - 缓存Key前缀（LOGIN_TOKEN_KEY、USER_INFO_KEY等）
   - 过期时间配置（登录30分钟、验证码2分钟等）
   - 安全配置（密码错误最大次数等）
   - 20+个缓存相关常量

4. **ConstantsTestController.java** - 常量测试接口（186行）
   - 5个测试接口
   - 演示常量使用方法
   - 对比魔法值和常量的区别

### 使用示例

**场景1：判断用户状态**
```java
// ❌ 使用魔法值（不推荐）
if ("0".equals(user.getStatus())) {
    // 正常用户
}

// ✅ 使用常量（推荐）
if (UserConstants.USER_NORMAL.equals(user.getStatus())) {
    // 正常用户
}
```

**场景2：生成Redis缓存Key**
```java
// ❌ 使用魔法值（不推荐）
String key = "login_token:" + userId;

// ✅ 使用常量（推荐）
String key = CacheConstants.LOGIN_TOKEN_KEY + userId;
```

**场景3：验证用户名长度**
```java
// ❌ 使用魔法值（不推荐）
if (userName.length() < 2 || userName.length() > 20) {
    throw new ServiceException("用户名长度应在2-20个字符之间");
}

// ✅ 使用常量（推荐）
if (userName.length() < UserConstants.USERNAME_MIN_LENGTH 
    || userName.length() > UserConstants.USERNAME_MAX_LENGTH) {
    throw new ServiceException("用户名长度应在" + UserConstants.USERNAME_MIN_LENGTH 
        + "-" + UserConstants.USERNAME_MAX_LENGTH + "个字符之间");
}
```

**场景4：判断菜单类型**
```java
// ❌ 使用魔法值（不推荐）
if ("M".equals(menu.getMenuType())) {
    // 目录
} else if ("C".equals(menu.getMenuType())) {
    // 菜单
} else if ("F".equals(menu.getMenuType())) {
    // 按钮
}

// ✅ 使用常量（推荐）
if (UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
    // 目录
} else if (UserConstants.TYPE_MENU.equals(menu.getMenuType())) {
    // 菜单
} else if (UserConstants.TYPE_BUTTON.equals(menu.getMenuType())) {
    // 按钮
}
```

### 常量类的好处

1. **提高代码可读性**
   - 一眼就能看懂常量的含义
   - 不需要注释说明

2. **便于维护**
   - 修改常量值只需改一处
   - 避免漏改导致的bug

3. **IDE支持**
   - 自动提示常量列表
   - 自动import
   - 重构更安全

4. **避免魔法值**
   - 减少硬编码
   - 降低出错概率

---

## 🎉 第2阶段100%完成！

### 最终统计

| 子阶段 | 状态 | 文件数 | 代码行数 | 接口数 | 时间 |
|-------|------|--------|---------|--------|------|
| 2.1 统一响应 | ✅ | 2 | 449 | 8 | 1小时 |
| 2.2 基础工具 | ✅ | 4 | 1,356 | 5 | 2小时 |
| 2.3 通用实体 | ✅ | 5 | 751 | 5 | 1小时 |
| 2.4 异常处理 | ✅ | 4 | 681 | 8 | 2小时 |
| 2.5 常量定义 | ✅ | 4 | 604 | 5 | 0.5小时 |
| 前后端联调 | ✅ | 1 | 60 | - | 1小时 |
| **第2阶段总计** | **✅** | **20** | **3,901** | **31** | **7.5小时** |

### 成果总览

- ✅ **20个Java类**（3,901行代码）
- ✅ **31个测试接口**
- ✅ **30个核心知识点**
- ✅ **15个文档文件**
- ✅ **1天完成**（预计1-2天）

---

## 🎊 恭喜你！

**你已经成功完成了第2阶段的学习！**

你现在掌握了：
- ✅ 统一响应格式设计
- ✅ 常用工具类实现
- ✅ 实体类继承体系
- ✅ 分页功能封装
- ✅ 全局异常处理
- ✅ 常量管理规范
- ✅ 前后端分离架构

**准备好进入第3阶段了吗？** 🚀

---

**下一步：** 阅读 `docs/phase-03-数据访问.md` 开始第3阶段学习

**预告第3阶段：**
- 集成 MyBatis-Plus
- 配置 Druid 数据源
- 实现数据库 CRUD 操作
- 分页查询实战

**加油！** 💪

