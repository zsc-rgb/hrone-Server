# 📚 MyBatis-Plus 学习教程

> 从零开始，一步一步学会 MyBatis-Plus

---

## 📖 目录

1. [MyBatis-Plus 简介](#1-mybatis-plus-简介)
2. [核心注解](#2-核心注解)
3. [BaseMapper 使用](#3-basemapper-使用)
4. [条件构造器](#4-条件构造器)
5. [分页查询](#5-分页查询)
6. [Service 层](#6-service-层)
7. [实战案例](#7-实战案例)
8. [常见问题](#8-常见问题)

---

## 1. MyBatis-Plus 简介

### 什么是 MyBatis-Plus？

MyBatis-Plus（简称 MP）是一个 MyBatis 的增强工具，在 MyBatis 的基础上**只做增强不做改变**。

**核心特性：**
- 🚀 **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响
- 💪 **损耗小**：启动即会自动注入基本CURD，性能基本无损耗
- 🛠️ **强大的CRUD操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分CRUD操作
- 🔧 **支持Lambda形式调用**：通过Lambda表达式，方便的编写各类查询条件
- 📄 **支持主键自动生成**：支持多达4种主键策略
- 🔐 **内置分页插件**：基于 MyBatis 物理分页

### 为什么要用 MyBatis-Plus？

**传统 MyBatis 写法：**
```xml
<!-- 需要写大量的XML -->
<select id="selectUserById" resultType="User">
    SELECT * FROM sys_user WHERE user_id = #{userId}
</select>

<select id="selectUserList" resultType="User">
    SELECT * FROM sys_user WHERE del_flag = '0'
    <if test="userName != null">
        AND user_name LIKE CONCAT('%', #{userName}, '%')
    </if>
    ORDER BY create_time DESC
</select>

<insert id="insertUser">
    INSERT INTO sys_user (user_name, nick_name, ...)
    VALUES (#{userName}, #{nickName}, ...)
</insert>
```

**MyBatis-Plus 写法：**
```java
// 不需要写XML！直接调用方法！
User user = userMapper.selectById(userId);

LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getDelFlag, "0")
       .like(User::getUserName, userName)
       .orderByDesc(User::getCreateTime);
List<User> list = userMapper.selectList(wrapper);

userMapper.insert(user);
```

**省去了 90% 的 XML 配置！** 🎉

---

## 2. 核心注解

### 2.1 @TableName

**作用：** 指定实体类对应的数据库表名

**示例：**
```java
@TableName("sys_user")  // 指定表名为 sys_user
public class SysUser extends BaseEntity {
    // ...
}
```

**说明：**
- 如果不加注解，默认使用类名的下划线命名作为表名
- 例如：`SysUser` 默认对应表 `sys_user`
- 如果表名和类名不一致，必须使用此注解

---

### 2.2 @TableId

**作用：** 指定主键字段和主键生成策略

**示例：**
```java
@TableId(value = "user_id", type = IdType.AUTO)
private Long userId;
```

**参数说明：**
- `value`：数据库主键字段名
- `type`：主键生成策略

**主键策略（IdType）：**

| 类型 | 说明 | 示例 |
|------|------|------|
| `AUTO` | 数据库自增 | MySQL 的 AUTO_INCREMENT |
| `NONE` | 无状态 | 跟随全局配置 |
| `INPUT` | 手动输入 | 需要自己设置ID |
| `ASSIGN_ID` | 雪花算法 | 分布式ID，默认 |
| `ASSIGN_UUID` | UUID | 32位UUID字符串 |

**推荐使用：**
- 单体应用：`IdType.AUTO`（数据库自增）
- 分布式应用：`IdType.ASSIGN_ID`（雪花算法）

---

### 2.3 @TableField

**作用：** 指定字段映射规则

**常用场景：**

#### 场景1：字段不存在于数据库

```java
@TableField(exist = false)
private String searchValue;  // 这个字段不在数据库表中

@TableField(exist = false)
private Map<String, Object> params;  // 临时存储参数
```

#### 场景2：字段名和数据库列名不一致

```java
@TableField("nick_name")  // 数据库列名
private String nickName;   // Java字段名
```

**说明：**
- 默认使用驼峰转下划线（`nickName` → `nick_name`）
- 如果符合规则，不需要加注解

#### 场景3：字段不参与查询

```java
@TableField(select = false)
private String password;  // 查询时不返回密码字段
```

---

### 2.4 @TableLogic

**作用：** 逻辑删除字段

**示例：**
```java
@TableLogic
private String delFlag;  // 0=未删除，2=已删除
```

**效果：**
```java
// 执行删除
userMapper.deleteById(1);

// 实际执行的SQL：
UPDATE sys_user SET del_flag = '2' WHERE user_id = 1

// 执行查询
userMapper.selectList(null);

// 实际执行的SQL：
SELECT * FROM sys_user WHERE del_flag = '0'
```

**自动过滤已删除的数据！** ✅

---

## 3. BaseMapper 使用

### 3.1 什么是 BaseMapper？

BaseMapper 是 MyBatis-Plus 提供的基础 Mapper 接口，包含了常用的 CRUD 方法。

**使用方式：**
```java
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 不需要写任何方法，就已经有了CRUD功能！
}
```

---

### 3.2 BaseMapper 提供的方法

#### 🟢 插入（Insert）

| 方法 | 说明 | 示例 |
|------|------|------|
| `int insert(T entity)` | 插入一条记录 | `userMapper.insert(user)` |

**示例：**
```java
SysUser user = new SysUser();
user.setUserName("test001");
user.setNickName("测试用户");
user.setEmail("test@hrone.com");

int rows = userMapper.insert(user);
// rows = 1 表示插入成功
// user.getUserId() 会自动填充（如果是自增主键）
```

---

#### 🔴 删除（Delete）

| 方法 | 说明 | 示例 |
|------|------|------|
| `int deleteById(Serializable id)` | 根据ID删除 | `userMapper.deleteById(1)` |
| `int deleteByMap(Map<String, Object> map)` | 根据Map条件删除 | - |
| `int delete(Wrapper<T> wrapper)` | 根据条件删除 | - |
| `int deleteBatchIds(Collection<? extends Serializable> ids)` | 批量删除 | `userMapper.deleteBatchIds(Arrays.asList(1,2,3))` |

**示例：**
```java
// 1. 根据ID删除
int rows = userMapper.deleteById(1);

// 2. 批量删除
List<Long> ids = Arrays.asList(1L, 2L, 3L);
int rows = userMapper.deleteBatchIds(ids);

// 3. 条件删除
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "1");  // 删除所有停用的用户
int rows = userMapper.delete(wrapper);
```

---

#### 🟡 更新（Update）

| 方法 | 说明 | 示例 |
|------|------|------|
| `int updateById(T entity)` | 根据ID更新 | `userMapper.updateById(user)` |
| `int update(T entity, Wrapper<T> wrapper)` | 根据条件更新 | - |

**示例：**
```java
// 1. 根据ID更新
SysUser user = new SysUser();
user.setUserId(1L);
user.setNickName("新昵称");  // 只更新昵称
user.setEmail("new@hrone.com");

int rows = userMapper.updateById(user);
// 只更新设置了值的字段，null字段不更新

// 2. 条件更新
SysUser user = new SysUser();
user.setStatus("1");  // 设置为停用

LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getUserName, "test");

int rows = userMapper.update(user, wrapper);
// 更新所有用户名包含test的用户状态为停用
```

---

#### 🔵 查询（Select）

| 方法 | 说明 | 返回类型 |
|------|------|---------|
| `T selectById(Serializable id)` | 根据ID查询 | 单个对象 |
| `List<T> selectBatchIds(Collection<? extends Serializable> ids)` | 批量查询 | 列表 |
| `List<T> selectByMap(Map<String, Object> map)` | 根据Map查询 | 列表 |
| `T selectOne(Wrapper<T> wrapper)` | 查询一条记录 | 单个对象 |
| `List<T> selectList(Wrapper<T> wrapper)` | 查询列表 | 列表 |
| `Long selectCount(Wrapper<T> wrapper)` | 查询总数 | 数量 |
| `IPage<T> selectPage(IPage<T> page, Wrapper<T> wrapper)` | 分页查询 | 分页对象 |

**示例：**
```java
// 1. 根据ID查询
SysUser user = userMapper.selectById(1);

// 2. 批量查询
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<SysUser> users = userMapper.selectBatchIds(ids);

// 3. 查询所有
List<SysUser> allUsers = userMapper.selectList(null);

// 4. 条件查询
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0");
List<SysUser> normalUsers = userMapper.selectList(wrapper);

// 5. 查询总数
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0");
Long count = userMapper.selectCount(wrapper);
```

---

## 4. 条件构造器

### 4.1 为什么要用条件构造器？

**传统方式：**
```java
// 需要拼接SQL字符串，容易出错
String sql = "SELECT * FROM sys_user WHERE 1=1";
if (userName != null) {
    sql += " AND user_name LIKE '%" + userName + "%'";  // SQL注入风险！
}
```

**MyBatis-Plus 方式：**
```java
// 类型安全，防SQL注入
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.like(SysUser::getUserName, userName);
```

---

### 4.2 LambdaQueryWrapper 常用方法

#### 🟢 比较操作

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `eq(字段, 值)` | 等于 = | `WHERE user_id = 1` |
| `ne(字段, 值)` | 不等于 != | `WHERE user_id != 1` |
| `gt(字段, 值)` | 大于 > | `WHERE age > 18` |
| `ge(字段, 值)` | 大于等于 >= | `WHERE age >= 18` |
| `lt(字段, 值)` | 小于 < | `WHERE age < 60` |
| `le(字段, 值)` | 小于等于 <= | `WHERE age <= 60` |
| `between(字段, 值1, 值2)` | BETWEEN | `WHERE age BETWEEN 18 AND 60` |

**示例：**
```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

// 1. 等于
wrapper.eq(SysUser::getUserId, 1);
// SQL: WHERE user_id = 1

// 2. 大于
wrapper.gt(SysUser::getUserId, 10);
// SQL: WHERE user_id > 10

// 3. BETWEEN
wrapper.between(SysUser::getUserId, 1, 100);
// SQL: WHERE user_id BETWEEN 1 AND 100
```

---

#### 🟡 模糊查询

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `like(字段, 值)` | 模糊查询 | `WHERE user_name LIKE '%张%'` |
| `likeLeft(字段, 值)` | 左模糊 | `WHERE user_name LIKE '%张'` |
| `likeRight(字段, 值)` | 右模糊 | `WHERE user_name LIKE '张%'` |
| `notLike(字段, 值)` | 不包含 | `WHERE user_name NOT LIKE '%张%'` |

**示例：**
```java
// 1. 模糊查询（两边模糊）
wrapper.like(SysUser::getUserName, "张");
// SQL: WHERE user_name LIKE '%张%'

// 2. 左模糊
wrapper.likeLeft(SysUser::getUserName, "三");
// SQL: WHERE user_name LIKE '%三'

// 3. 右模糊
wrapper.likeRight(SysUser::getUserName, "张");
// SQL: WHERE user_name LIKE '张%'
```

---

#### 🔵 NULL值判断

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `isNull(字段)` | 为NULL | `WHERE email IS NULL` |
| `isNotNull(字段)` | 不为NULL | `WHERE email IS NOT NULL` |

**示例：**
```java
// 1. 查询邮箱为空的用户
wrapper.isNull(SysUser::getEmail);
// SQL: WHERE email IS NULL

// 2. 查询邮箱不为空的用户
wrapper.isNotNull(SysUser::getEmail);
// SQL: WHERE email IS NOT NULL
```

---

#### 🟣 IN查询

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `in(字段, 值列表)` | IN | `WHERE user_id IN (1,2,3)` |
| `notIn(字段, 值列表)` | NOT IN | `WHERE user_id NOT IN (1,2,3)` |

**示例：**
```java
// 1. IN查询
List<Long> ids = Arrays.asList(1L, 2L, 3L);
wrapper.in(SysUser::getUserId, ids);
// SQL: WHERE user_id IN (1, 2, 3)

// 2. NOT IN
wrapper.notIn(SysUser::getUserId, ids);
// SQL: WHERE user_id NOT IN (1, 2, 3)
```

---

#### 🟠 排序

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `orderByAsc(字段)` | 升序 | `ORDER BY create_time ASC` |
| `orderByDesc(字段)` | 降序 | `ORDER BY create_time DESC` |
| `orderBy(是否升序, 字段)` | 排序 | - |

**示例：**
```java
// 1. 升序
wrapper.orderByAsc(SysUser::getCreateTime);
// SQL: ORDER BY create_time ASC

// 2. 降序
wrapper.orderByDesc(SysUser::getCreateTime);
// SQL: ORDER BY create_time DESC

// 3. 多字段排序
wrapper.orderByDesc(SysUser::getCreateTime)
       .orderByAsc(SysUser::getUserId);
// SQL: ORDER BY create_time DESC, user_id ASC
```

---

#### 🔷 逻辑操作

| 方法 | 说明 | SQL示例 |
|------|------|---------|
| `and()` | AND | `WHERE a=1 AND b=2` |
| `or()` | OR | `WHERE a=1 OR b=2` |

**示例：**
```java
// 1. AND条件（默认就是AND）
wrapper.eq(SysUser::getStatus, "0")
       .eq(SysUser::getDelFlag, "0");
// SQL: WHERE status = '0' AND del_flag = '0'

// 2. OR条件
wrapper.eq(SysUser::getUserName, "admin")
       .or()
       .eq(SysUser::getUserName, "zhangsan");
// SQL: WHERE user_name = 'admin' OR user_name = 'zhangsan'

// 3. 复杂条件
wrapper.eq(SysUser::getStatus, "0")
       .and(w -> w.eq(SysUser::getUserName, "admin")
                  .or()
                  .like(SysUser::getNickName, "管理"));
// SQL: WHERE status = '0' AND (user_name = 'admin' OR nick_name LIKE '%管理%')
```

---

### 4.3 综合示例

```java
/**
 * 查询满足以下条件的用户：
 * 1. 状态为正常
 * 2. 用户名包含"张"或邮箱包含"hrone"
 * 3. 年龄在18-60之间
 * 4. 未删除
 * 5. 按创建时间倒序
 */
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

wrapper.eq(SysUser::getStatus, "0")  // 状态正常
       .and(w -> w.like(SysUser::getUserName, "张")  // 用户名包含张
                  .or()
                  .like(SysUser::getEmail, "hrone"))  // 或邮箱包含hrone
       .between(SysUser::getAge, 18, 60)  // 年龄18-60
       .eq(SysUser::getDelFlag, "0")  // 未删除
       .orderByDesc(SysUser::getCreateTime);  // 按创建时间倒序

List<SysUser> users = userMapper.selectList(wrapper);
```

**生成的SQL：**
```sql
SELECT * FROM sys_user 
WHERE status = '0' 
  AND (user_name LIKE '%张%' OR email LIKE '%hrone%')
  AND age BETWEEN 18 AND 60
  AND del_flag = '0'
ORDER BY create_time DESC
```

---

## 5. 分页查询

### 5.1 配置分页插件

**已经配置好了！** 在 `MybatisPlusConfig.java` 中：

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    // 1. 创建MyBatis-Plus主拦截器
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    
    // 添加分页插件
    PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
    //设置数据库类型为Mysql 
    paginationInnerInterceptor.setDbType(DbType.MYSQL);
    //将分页插件添加到主拦截器中
    interceptor.addInnerInterceptor(paginationInnerInterceptor);
    //返回配置好的拦截器
    return interceptor;
}
```

---

### 5.2 使用分页查询

**步骤：**
1. 创建 Page 对象（指定页码和每页大小）
2. 调用 selectPage 方法
3. 从 IPage 中获取分页信息

**示例：**
```java
// 1. 创建分页对象
Page<SysUser> page = new Page<>();
page.setCurrent(1);   // 当前页码（从1开始）
page.setSize(10);     // 每页大小

// 或者简写
Page<SysUser> page = new Page<>(1, 10);

// 2. 构建查询条件
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0");

// 3. 执行分页查询
IPage<SysUser> pageResult = userMapper.selectPage(page, wrapper);

// 4. 获取分页信息
long total = pageResult.getTotal();         // 总记录数
List<SysUser> records = pageResult.getRecords();  // 当前页数据
long pages = pageResult.getPages();         // 总页数
long current = pageResult.getCurrent();     // 当前页码
long size = pageResult.getSize();           // 每页大小
```

**生成的SQL：**
```sql
-- 1. 查询总数
SELECT COUNT(*) FROM sys_user WHERE status = '0';

-- 2. 查询数据
SELECT * FROM sys_user WHERE status = '0' LIMIT 0, 10;
```

**MyBatis-Plus 自动加上了 LIMIT！** ✅

---

### 5.3 在项目中的应用

**Controller 层：**
```java
@GetMapping("/page")
public AjaxResult page(
    @RequestParam(defaultValue = "1") Integer pageNum,
    @RequestParam(defaultValue = "10") Integer pageSize
) {
    // 创建分页对象
    Page<SysUser> page = new Page<>(pageNum, pageSize);
    
    // 执行分页查询
    IPage<SysUser> result = userService.page(page);
    
    // 返回分页数据
    return AjaxResult.success()
            .put("total", result.getTotal())
            .put("rows", result.getRecords())
            .put("pages", result.getPages());
}
```

**访问：**
```
GET /system/user/page?pageNum=2&pageSize=20

响应：
{
  "code": 200,
  "msg": "操作成功",
  "total": 100,
  "rows": [ ... 20条数据 ... ],
  "pages": 5
}
```

---

## 6. Service 层

### 6.1 IService 和 ServiceImpl

**MyBatis-Plus 提供了 Service 层的封装：**

```java
// Service 接口
public interface ISysUserService extends IService<SysUser> {
    // 继承 IService 后自动拥有批量操作方法
}

// Service 实现类
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
        implements ISysUserService {
    // 继承 ServiceImpl 后自动实现 IService 的所有方法
}
```

---

### 6.2 IService 提供的方法

#### 🟢 保存（Save）

| 方法 | 说明 |
|------|------|
| `boolean save(T entity)` | 插入一条记录 |
| `boolean saveBatch(Collection<T> entityList)` | 批量插入 |
| `boolean saveOrUpdate(T entity)` | 有则更新，无则插入 |

**示例：**
```java
// 1. 保存单个
SysUser user = new SysUser();
user.setUserName("test");
boolean result = userService.save(user);

// 2. 批量保存
List<SysUser> users = new ArrayList<>();
// ... 添加多个用户
boolean result = userService.saveBatch(users);

// 3. 保存或更新
user.setUserId(1L);  // 如果ID=1的用户存在，则更新；否则插入
boolean result = userService.saveOrUpdate(user);
```

---

#### 🔴 删除（Remove）

| 方法 | 说明 |
|------|------|
| `boolean removeById(Serializable id)` | 根据ID删除 |
| `boolean removeByIds(Collection<? extends Serializable> ids)` | 批量删除 |
| `boolean remove(Wrapper<T> wrapper)` | 根据条件删除 |

**示例：**
```java
// 1. 根据ID删除
boolean result = userService.removeById(1);

// 2. 批量删除
List<Long> ids = Arrays.asList(1L, 2L, 3L);
boolean result = userService.removeByIds(ids);
```

---

#### 🟡 更新（Update）

| 方法 | 说明 |
|------|------|
| `boolean updateById(T entity)` | 根据ID更新 |
| `boolean updateBatchById(Collection<T> entityList)` | 批量更新 |
| `boolean update(T entity, Wrapper<T> wrapper)` | 根据条件更新 |

**示例：**
```java
// 1. 根据ID更新
SysUser user = new SysUser();
user.setUserId(1L);
user.setNickName("新昵称");
boolean result = userService.updateById(user);

// 2. 批量更新
List<SysUser> users = new ArrayList<>();
// ... 添加多个用户
boolean result = userService.updateBatchById(users);
```

---

#### 🔵 查询（Get/List）

| 方法 | 说明 | 返回类型 |
|------|------|---------|
| `T getById(Serializable id)` | 根据ID查询 | 单个对象 |
| `T getOne(Wrapper<T> wrapper)` | 查询一条记录 | 单个对象 |
| `List<T> list()` | 查询所有 | 列表 |
| `List<T> list(Wrapper<T> wrapper)` | 条件查询 | 列表 |
| `List<T> listByIds(Collection<? extends Serializable> ids)` | 批量查询 | 列表 |
| `Long count()` | 查询总数 | 数量 |
| `Long count(Wrapper<T> wrapper)` | 条件查询总数 | 数量 |

**示例：**
```java
// 1. 根据ID查询
SysUser user = userService.getById(1);

// 2. 查询所有
List<SysUser> allUsers = userService.list();

// 3. 条件查询
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0");
List<SysUser> normalUsers = userService.list(wrapper);

// 4. 查询总数
Long total = userService.count();
```

---

#### 📄 分页查询（Page）

| 方法 | 说明 |
|------|------|
| `IPage<T> page(IPage<T> page)` | 分页查询所有 |
| `IPage<T> page(IPage<T> page, Wrapper<T> wrapper)` | 条件分页查询 |

**示例：**
```java
// 1. 分页查询所有
Page<SysUser> page = new Page<>(1, 10);
IPage<SysUser> result = userService.page(page);

// 2. 条件分页查询
Page<SysUser> page = new Page<>(1, 10);
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0");
IPage<SysUser> result = userService.page(page, wrapper);
```

---

## 7. 实战案例

### 案例1：用户列表查询（支持多条件）

**需求：**
- 支持按用户名模糊查询
- 支持按手机号精确查询
- 支持按状态筛选
- 只查询未删除的数据
- 按创建时间倒序

**实现：**
```java
@Override
public List<SysUser> selectUserList(SysUser user) {
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    
    // 1. 用户名模糊查询（如果传了userName参数）
    if (StringUtils.isNotEmpty(user.getUserName())) {
        wrapper.like(SysUser::getUserName, user.getUserName());
    }
    
    // 2. 手机号精确查询
    if (StringUtils.isNotEmpty(user.getPhone())) {
        wrapper.eq(SysUser::getPhone, user.getPhone());
    }
    
    // 3. 状态筛选
    if (StringUtils.isNotEmpty(user.getStatus())) {
        wrapper.eq(SysUser::getStatus, user.getStatus());
    }
    
    // 4. 只查询未删除的
    wrapper.eq(SysUser::getDelFlag, "0");
    
    // 5. 按创建时间倒序
    wrapper.orderByDesc(SysUser::getCreateTime);
    
    return this.list(wrapper);
}
```

**调用：**
```java
SysUser queryUser = new SysUser();
queryUser.setUserName("张");  // 查询用户名包含"张"的用户
queryUser.setStatus("0");    // 状态为正常

List<SysUser> users = userService.selectUserList(queryUser);
```

---

### 案例2：分页查询（带条件）

**需求：**
- 分页查询用户
- 支持按用户名搜索
- 只查询正常状态的用户

**实现：**
```java
@GetMapping("/page")
public AjaxResult page(
    @RequestParam(defaultValue = "1") Integer pageNum,
    @RequestParam(defaultValue = "10") Integer pageSize,
    String userName
) {
    // 1. 创建分页对象
    Page<SysUser> page = new Page<>(pageNum, pageSize);
    
    // 2. 构建查询条件
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.isNotEmpty(userName)) {
        wrapper.like(SysUser::getUserName, userName);
    }
    wrapper.eq(SysUser::getStatus, "0");
    wrapper.eq(SysUser::getDelFlag, "0");
    
    // 3. 执行分页查询
    IPage<SysUser> result = userService.page(page, wrapper);
    
    // 4. 返回结果
    return AjaxResult.success()
            .put("total", result.getTotal())
            .put("rows", result.getRecords())
            .put("pages", result.getPages());
}
```

---

### 案例3：新增用户（带业务校验）

**需求：**
- 校验用户名是否唯一
- 设置默认值
- 记录创建时间

**实现：**
```java
@Override
public int insertUser(SysUser user) {
    // 1. 校验用户名唯一性
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getUserName, user.getUserName());
    long count = this.count(wrapper);
    
    if (count > 0) {
        throw new ServiceException("用户名已存在");
    }
    
    // 2. 设置默认值
    if (StringUtils.isEmpty(user.getStatus())) {
        user.setStatus("0");  // 默认正常状态
    }
    user.setDelFlag("0");  // 未删除
    user.setCreateTime(new Date());
    
    // 3. 保存用户
    boolean result = this.save(user);
    return result ? 1 : 0;
}
```

---

### 案例4：更新用户（带权限校验）

**需求：**
- 不能修改超级管理员
- 检查用户是否存在
- 记录更新时间

**实现：**
```java
@Override
public int updateUser(SysUser user) {
    // 1. 检查用户是否存在
    SysUser existUser = this.getById(user.getUserId());
    if (existUser == null) {
        throw new ServiceException("用户不存在", 404);
    }
    
    // 2. 不能修改超级管理员
    if (UserConstants.ADMIN_ID.equals(user.getUserId())) {
        throw new ServiceException("不允许修改超级管理员", 403);
    }
    
    // 3. 设置更新时间
    user.setUpdateTime(new Date());
    
    // 4. 更新用户
    boolean result = this.updateById(user);
    return result ? 1 : 0;
}
```

---

### 案例5：逻辑删除

**需求：**
- 不是真正删除，而是设置删除标志
- 不能删除超级管理员

**实现：**
```java
@Override
public int deleteUserById(Long userId) {
    // 1. 不能删除超级管理员
    if (UserConstants.ADMIN_ID.equals(userId)) {
        throw new ServiceException("不允许删除超级管理员", 403);
    }
    
    // 2. 检查用户是否存在
    SysUser user = this.getById(userId);
    if (user == null) {
        throw new ServiceException("用户不存在", 404);
    }
    
    // 3. 逻辑删除（设置删除标志）
    user.setDelFlag("2");
    user.setUpdateTime(new Date());
    boolean result = this.updateById(user);
    
    return result ? 1 : 0;
}
```

---

## 8. 常见问题

### Q1: BaseMapper 和 Mapper.xml 可以一起用吗？

**答：** 可以！

```java
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    // 使用BaseMapper的方法（不需要XML）
    // selectById、insert、updateById等
    
    // 自定义方法（需要在XML中写SQL）
    List<SysUser> selectUserWithRole();
}
```

**对应的 XML：**
```xml
<mapper namespace="com.hrone.system.mapper.SysUserMapper">
    <select id="selectUserWithRole" resultType="SysUser">
        SELECT u.*, r.role_name 
        FROM sys_user u 
        LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
        LEFT JOIN sys_role r ON ur.role_id = r.role_id
    </select>
</mapper>
```

---

### Q2: LambdaQueryWrapper 和 QueryWrapper 的区别？

**QueryWrapper（字符串方式）：**
```java
QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
wrapper.eq("user_name", "admin");  // 字符串，容易写错
```

**LambdaQueryWrapper（Lambda方式）：**
```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getUserName, "admin");  // Lambda，编译期检查
```

**推荐使用 LambdaQueryWrapper：**
- ✅ 类型安全
- ✅ IDE自动提示
- ✅ 重构友好

---

### Q3: 字段为NULL时，会更新为NULL吗？

**默认行为：** NULL字段不更新

```java
SysUser user = new SysUser();
user.setUserId(1L);
user.setNickName("新昵称");  // 设置了值
user.setEmail(null);       // NULL值

userService.updateById(user);
```

**生成的SQL：**
```sql
UPDATE sys_user 
SET nick_name = '新昵称'  -- 只更新有值的字段
-- email不会被更新
WHERE user_id = 1
```

**如果要更新NULL：**
```java
UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
wrapper.eq("user_id", 1)
       .set("email", null);  // 强制设置为NULL

userService.update(wrapper);
```

---

### Q4: 如何处理逻辑删除？

**方式1：使用@TableLogic注解（推荐）**

```java
@TableLogic
private String delFlag;  // 0=正常，2=删除
```

**配置：**
```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: delFlag
      logic-delete-value: 2
      logic-not-delete-value: 0
```

**效果：**
```java
// 执行删除
userMapper.deleteById(1);
// 实际SQL: UPDATE sys_user SET del_flag = '2' WHERE user_id = 1

// 执行查询
userMapper.selectList(null);
// 实际SQL: SELECT * FROM sys_user WHERE del_flag = '0'
```

**方式2：手动处理（我们项目用的）**

```java
public int deleteUserById(Long userId) {
    SysUser user = this.getById(userId);
    user.setDelFlag("2");  // 手动设置删除标志
    return this.updateById(user) ? 1 : 0;
}
```

---

### Q5: 分页查询时如何不查询总数？

**场景：** 数据量大时，查询总数很慢

```java
Page<SysUser> page = new Page<>(1, 10);
page.setSearchCount(false);  // 不查询总数

IPage<SysUser> result = userService.page(page);
// 只查询数据，不查询总数
```

---

### Q6: 如何执行原生SQL？

**使用 @Select 注解：**
```java
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    @Select("SELECT * FROM sys_user WHERE user_name = #{userName}")
    SysUser selectByUserName(@Param("userName") String userName);
}
```

**或者在XML中：**
```xml
<select id="selectByUserName" resultType="SysUser">
    SELECT * FROM sys_user WHERE user_name = #{userName}
</select>
```

---

## 📝 学习路径

### 第1步：理解基础概念（30分钟）

- [ ] 了解 MyBatis-Plus 的作用
- [ ] 理解 BaseMapper 的概念
- [ ] 认识核心注解

**实践：** 查看项目中的 `SysUser.java` 和 `SysUserMapper.java`

---

### 第2步：掌握 CRUD 操作（1小时）

- [ ] 学习 BaseMapper 的增删改查方法
- [ ] 理解每个方法的用法
- [ ] 测试基本的CRUD接口

**实践：** 测试以下接口
```
GET  /system/user/1           # 查询
POST /system/user             # 新增
PUT  /system/user             # 修改
DELETE /system/user/2         # 删除
```

---

### 第3步：学习条件构造器（1小时）

- [ ] 掌握 LambdaQueryWrapper 的使用
- [ ] 学习常用的条件方法（eq、like、between等）
- [ ] 理解链式调用

**实践：** 查看 `SysUserServiceImpl.java` 中的查询方法

---

### 第4步：掌握分页查询（30分钟）

- [ ] 理解 Page 对象
- [ ] 学习分页配置
- [ ] 掌握分页信息的获取

**实践：** 测试分页接口
```
GET /system/user/page?pageNum=1&pageSize=3
```

---

### 第5步：熟悉 Service 层（1小时）

- [ ] 理解 IService 接口
- [ ] 学习 ServiceImpl 的使用
- [ ] 掌握批量操作

**实践：** 查看 `ISysUserService.java` 和 `SysUserServiceImpl.java`

---

### 第6步：实战练习（2小时）

- [ ] 实现一个完整的用户管理功能
- [ ] 包含增删改查
- [ ] 包含分页查询
- [ ] 包含条件查询

**实践：** 尝试自己写一个 `SysRole`（角色）模块

---

## 🎓 进阶学习

### 1. 乐观锁

```java
@Version
private Integer version;  // 版本号

// 更新时自动 +1，并作为更新条件
// UPDATE ... SET version = version + 1 WHERE version = 旧版本
```

### 2. 自动填充

```java
@TableField(fill = FieldFill.INSERT)
private Date createTime;  // 插入时自动填充

@TableField(fill = FieldFill.UPDATE)
private Date updateTime;  // 更新时自动填充
```

### 3. 多租户

```java
// 自动添加租户ID条件
// SELECT * FROM sys_user WHERE tenant_id = 当前租户
```

### 4. 数据权限

```java
// 自动添加数据权限过滤
// SELECT * FROM sys_user WHERE dept_id IN (...)
```

---

## 📖 官方文档

- **官网：** https://baomidou.com/
- **快速开始：** https://baomidou.com/pages/226c21/
- **核心功能：** https://baomidou.com/pages/49cc81/
- **插件扩展：** https://baomidou.com/pages/2976a3/

---

## 🎯 学习建议

### 1. 边学边练

不要只看文档，一定要：
- ✅ 启动项目
- ✅ 测试接口
- ✅ 查看SQL日志
- ✅ 修改代码实验

### 2. 看SQL日志

**配置已开启SQL日志打印：**
```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**控制台会打印SQL：**
```
==>  Preparing: SELECT * FROM sys_user WHERE del_flag = ? ORDER BY create_time DESC
==> Parameters: 0(String)
<==      Total: 5
```

**通过SQL理解MyBatis-Plus的工作原理！**

### 3. 对比传统方式

理解 MyBatis-Plus 省去了什么：
- ❌ 不需要写大量XML
- ❌ 不需要写重复的CRUD
- ❌ 不需要手动拼接SQL

**但保留了：**
- ✅ MyBatis 的所有功能
- ✅ 可以自定义SQL
- ✅ 完全的控制权

---

## 🧪 练习题

### 练习1：基础查询

**要求：** 查询所有正常状态的用户

```java
// 你的答案：
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
// TODO: 填写条件

List<SysUser> users = userMapper.selectList(wrapper);
```

<details>
<summary>点击查看答案</summary>

```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0")
       .eq(SysUser::getDelFlag, "0");

List<SysUser> users = userMapper.selectList(wrapper);
```
</details>

---

### 练习2：模糊查询

**要求：** 查询用户名包含"张"且邮箱包含"hrone"的用户

```java
// 你的答案：
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
// TODO: 填写条件

List<SysUser> users = userMapper.selectList(wrapper);
```

<details>
<summary>点击查看答案</summary>

```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.like(SysUser::getUserName, "张")
       .like(SysUser::getEmail, "hrone");

List<SysUser> users = userMapper.selectList(wrapper);
```
</details>

---

### 练习3：分页查询

**要求：** 查询第2页，每页5条，只查询正常用户，按创建时间倒序

```java
// 你的答案：
Page<SysUser> page = new Page<>(?, ?);
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
// TODO: 填写条件

IPage<SysUser> result = userService.page(page, wrapper);
```

<details>
<summary>点击查看答案</summary>

```java
Page<SysUser> page = new Page<>(2, 5);  // 第2页，每页5条
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getStatus, "0")
       .eq(SysUser::getDelFlag, "0")
       .orderByDesc(SysUser::getCreateTime);

IPage<SysUser> result = userService.page(page, wrapper);
```
</details>

---

## 🎊 恭喜！

学完这个教程，你已经掌握了 MyBatis-Plus 的核心功能！

**你现在可以：**
- ✅ 使用 BaseMapper 进行 CRUD
- ✅ 使用 LambdaQueryWrapper 构建条件
- ✅ 实现分页查询
- ✅ 使用 Service 层封装
- ✅ 处理逻辑删除

**下一步：**
- 阅读官方文档了解更多高级功能
- 在项目中多实践
- 尝试实现更复杂的业务

**加油！** 💪

