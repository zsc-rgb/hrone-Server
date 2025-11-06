# 🧪 MyBatis-Plus 实践指南

> 通过实际操作掌握 MyBatis-Plus

---

## 🎯 学习目标

通过实际测试项目中的接口，理解 MyBatis-Plus 的工作原理。

---

## 📝 前提条件

1. ✅ 项目已启动成功
2. ✅ 数据库已创建（hrone_db）
3. ✅ 测试数据已导入（5个用户）
4. ✅ 无编译错误

---

## 🧪 实践1：查看SQL日志

### 目的

理解 MyBatis-Plus 如何生成 SQL

### 步骤

1. **启动项目**
2. **访问接口：** `http://localhost:8080/system/user/list`
3. **查看控制台SQL日志：**

```sql
==>  Preparing: SELECT user_id,user_name,nick_name,email,phone,sex,avatar,password,status,del_flag,login_ip,login_date,create_by,create_time,update_by,update_time,remark FROM sys_user WHERE del_flag='0' AND (del_flag = ?) ORDER BY create_time DESC
==> Parameters: 0(String)
<==      Total: 5
```

### 分析

**SQL组成：**
1. `SELECT user_id,user_name,...` - 查询字段（自动映射）
2. `WHERE del_flag='0'` - 未删除条件
3. `AND (del_flag = ?)` - Lambda条件
4. `ORDER BY create_time DESC` - 排序

**MyBatis-Plus 做了什么：**
- ✅ 自动将 Java 字段转为数据库列名（驼峰→下划线）
- ✅ 自动过滤 `@TableField(exist = false)` 的字段
- ✅ 自动生成完整的 SELECT 语句

---

## 🧪 实践2：测试单表查询

### 目的

掌握 BaseMapper 的查询方法

### 测试用例

**1. 查询所有用户**

```
GET http://localhost:8080/system/user/list

预期：返回5个用户
```

**查看代码：** `SysUserServiceImpl.selectUserList()`

```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getDelFlag, "0")  // 只查询未删除的
       .orderByDesc(SysUser::getCreateTime);  // 按创建时间倒序

return this.list(wrapper);
```

---

**2. 根据ID查询**

```
GET http://localhost:8080/system/user/1

预期：返回admin用户信息
```

**查看代码：** `SysUserServiceImpl.selectUserById()`

```java
SysUser user = this.getById(userId);
```

**生成的SQL：**
```sql
SELECT * FROM sys_user WHERE user_id = 1
```

---

**3. 根据用户名查询**

```
GET http://localhost:8080/system/user/username/zhangsan

预期：返回张三的信息
```

**查看代码：** `SysUserServiceImpl.selectUserByUserName()`

```java
LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(SysUser::getUserName, userName)  // 用户名等于
       .eq(SysUser::getDelFlag, "0");       // 未删除

return this.getOne(wrapper);
```

---

## 🧪 实践3：测试条件查询

### 目的

理解 LambdaQueryWrapper 的使用

### 修改代码测试

**打开：** `SysUserServiceImpl.java`

**修改 `selectUserList` 方法，添加更多条件：**

```java
@Override
public List<SysUser> selectUserList(SysUser user) {
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    
    // 测试1：等于查询
    if (StringUtils.isNotEmpty(user.getStatus())) {
        wrapper.eq(SysUser::getStatus, user.getStatus());
    }
    
    // 测试2：模糊查询
    if (StringUtils.isNotEmpty(user.getUserName())) {
        wrapper.like(SysUser::getUserName, user.getUserName());
    }
    
    // 测试3：多字段OR查询
    if (StringUtils.isNotEmpty(user.getSearchValue())) {
        wrapper.and(w -> w.like(SysUser::getUserName, user.getSearchValue())
                          .or()
                          .like(SysUser::getNickName, user.getSearchValue())
                          .or()
                          .like(SysUser::getEmail, user.getSearchValue()));
    }
    
    wrapper.eq(SysUser::getDelFlag, "0");
    wrapper.orderByDesc(SysUser::getCreateTime);
    
    return this.list(wrapper);
}
```

**测试：**
```
# 1. 按状态查询
GET /system/user/list?status=0

# 2. 按用户名模糊查询
GET /system/user/list?userName=zhang

# 3. 全局搜索
GET /system/user/list?searchValue=admin
```

---

## 🧪 实践4：测试分页查询

### 目的

理解 MyBatis-Plus 的分页机制

### 测试步骤

**1. 第1页，每页3条**

```
GET http://localhost:8080/system/user/page?pageNum=1&pageSize=3

查看控制台SQL：
==>  Preparing: SELECT COUNT(*) AS total FROM sys_user
==> Parameters: 
<==      Total: 1

==>  Preparing: SELECT ... FROM sys_user LIMIT ?
==> Parameters: 3(Long)
<==      Total: 3
```

**分析：**
- 先执行 `COUNT(*)` 查询总数
- 再执行 `SELECT ... LIMIT 3` 查询数据
- MyBatis-Plus 自动添加了 LIMIT

---

**2. 第2页，每页3条**

```
GET http://localhost:8080/system/user/page?pageNum=2&pageSize=3

SQL：
SELECT ... FROM sys_user LIMIT 3, 3
                              ↑  ↑
                           偏移量 数量
```

**计算公式：**
```
偏移量 = (pageNum - 1) * pageSize
      = (2 - 1) * 3
      = 3
```

---

**3. 查看分页信息**

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 5,      // 总记录数
  "rows": [...],   // 当前页数据（3条）
  "pages": 2,      // 总页数 = Math.ceil(5 / 3) = 2
  "current": 2,    // 当前页码
  "size": 3        // 每页大小
}
```

---

## 🧪 实践5：测试新增功能

### 目的

理解 save 方法和业务校验

### 测试

**使用 Postman 或 curl：**

```bash
POST http://localhost:8080/system/user
Content-Type: application/json

{
  "userName": "testuser",
  "nickName": "测试账号",
  "email": "testuser@hrone.com",
  "phone": "13900139000",
  "password": "123456"
}
```

**查看控制台SQL：**
```sql
-- 1. 先查询用户名是否存在
SELECT COUNT(*) FROM sys_user WHERE user_name = 'testuser'

-- 2. 插入数据
INSERT INTO sys_user (user_name, nick_name, email, phone, password, status, del_flag, create_time)
VALUES ('testuser', '测试账号', 'testuser@hrone.com', '13900139000', '123456', '0', '0', '2025-11-06 10:50:00')
```

**验证：**
```
GET http://localhost:8080/system/user/list

应该能看到新增的用户！
```

---

**测试唯一性校验：**

再次发送相同的请求，应该返回：
```json
{
  "code": 500,
  "msg": "用户名已存在"
}
```

**业务校验生效！** ✅

---

## 🧪 实践6：测试更新功能

### 测试

```bash
PUT http://localhost:8080/system/user
Content-Type: application/json

{
  "userId": 2,
  "nickName": "新的昵称",
  "email": "newemail@hrone.com"
}
```

**查看SQL：**
```sql
-- 1. 先查询用户是否存在
SELECT * FROM sys_user WHERE user_id = 2

-- 2. 更新用户（只更新有值的字段）
UPDATE sys_user 
SET nick_name = '新的昵称', 
    email = 'newemail@hrone.com',
    update_time = '2025-11-06 10:52:00'
WHERE user_id = 2
```

**特点：**
- ✅ NULL字段不会更新
- ✅ 自动设置 update_time
- ✅ 只更新指定字段

---

**测试权限校验：**

尝试修改超级管理员：
```json
{
  "userId": 1,  // admin是超级管理员
  "nickName": "新昵称"
}
```

**应该返回：**
```json
{
  "code": 403,
  "msg": "不允许修改超级管理员"
}
```

**权限校验生效！** ✅

---

## 🧪 实践7：测试删除功能

### 测试

```
DELETE http://localhost:8080/system/user/2
```

**查看SQL：**
```sql
-- 1. 查询用户是否存在
SELECT * FROM sys_user WHERE user_id = 2

-- 2. 逻辑删除（不是真正删除）
UPDATE sys_user 
SET del_flag = '2',
    update_time = '2025-11-06 10:55:00'
WHERE user_id = 2
```

**验证：**
```
GET /system/user/list

返回结果中应该没有user_id=2的数据了
```

**但在数据库中：**
```sql
-- 在 Navicat 中执行
SELECT * FROM sys_user WHERE user_id = 2;

-- 数据还在，只是 del_flag = '2'
```

**逻辑删除生效！** ✅

---

## 🧪 实践8：对比查看数据库

### 目的

理解逻辑删除和物理删除的区别

### 操作

**1. 在 Navicat 中查看所有数据**

```sql
SELECT user_id, user_name, nick_name, del_flag 
FROM sys_user;
```

**结果：**
```
user_id | user_name | nick_name | del_flag
--------|-----------|-----------|----------
1       | admin     | 管理员     | 0
2       | zhangsan  | 张三       | 2  ← 已被逻辑删除
3       | lisi      | 李四       | 0
4       | wangwu    | 王五       | 0
5       | zhaoliu   | 赵六       | 0
```

**2. 通过接口查询**

```
GET /system/user/list

只返回 del_flag = '0' 的数据（1, 3, 4, 5）
```

**对比：**
- 数据库：5条数据（包含已删除的）
- 接口：4条数据（自动过滤已删除的）

---

## 📊 MyBatis-Plus vs 传统方式对比

### 场景：查询用户列表（支持条件查询和分页）

**传统 MyBatis 方式：**

**Mapper.xml（需要写150+行）：**
```xml
<mapper namespace="com.hrone.system.mapper.SysUserMapper">
    
    <!-- 查询用户列表 -->
    <select id="selectUserList" resultMap="UserResult">
        SELECT user_id, user_name, nick_name, email, phone, sex, 
               avatar, password, status, del_flag, login_ip, login_date,
               create_by, create_time, update_by, update_time, remark
        FROM sys_user
        WHERE del_flag = '0'
        <if test="userName != null and userName != ''">
            AND user_name LIKE CONCAT('%', #{userName}, '%')
        </if>
        <if test="phone != null and phone != ''">
            AND phone = #{phone}
        </if>
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
        ORDER BY create_time DESC
    </select>
    
    <!-- 查询用户详情 -->
    <select id="selectUserById" resultMap="UserResult">
        SELECT * FROM sys_user WHERE user_id = #{userId}
    </select>
    
    <!-- 插入用户 -->
    <insert id="insertUser">
        INSERT INTO sys_user (user_name, nick_name, email, phone, ...)
        VALUES (#{userName}, #{nickName}, #{email}, #{phone}, ...)
    </insert>
    
    <!-- 更新用户 -->
    <update id="updateUser">
        UPDATE sys_user
        <set>
            <if test="nickName != null">nick_name = #{nickName},</if>
            <if test="email != null">email = #{email},</if>
            ...
        </set>
        WHERE user_id = #{userId}
    </update>
    
    <!-- 删除用户 -->
    <delete id="deleteUser">
        DELETE FROM sys_user WHERE user_id = #{userId}
    </delete>
    
    <!-- ResultMap映射 -->
    <resultMap id="UserResult" type="SysUser">
        <id property="userId" column="user_id"/>
        <result property="userName" column="user_name"/>
        ...
    </resultMap>
</mapper>
```

**Mapper 接口：**
```java
public interface SysUserMapper {
    List<SysUser> selectUserList(SysUser user);
    SysUser selectUserById(Long userId);
    int insertUser(SysUser user);
    int updateUser(SysUser user);
    int deleteUser(Long userId);
}
```

**Service 实现：**
```java
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }
    
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }
    
    public int insertUser(SysUser user) {
        return userMapper.insertUser(user);
    }
    
    // ... 其他方法
}
```

---

**MyBatis-Plus 方式：**

**Mapper 接口（只需1行！）：**
```java
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 完成！不需要写任何方法！
}
```

**Service 实现：**
```java
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> 
        implements ISysUserService {
    
    public List<SysUser> selectUserList(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 动态条件
        if (StringUtils.isNotEmpty(user.getUserName())) {
            wrapper.like(SysUser::getUserName, user.getUserName());
        }
        wrapper.eq(SysUser::getDelFlag, "0");
        wrapper.orderByDesc(SysUser::getCreateTime);
        
        return this.list(wrapper);
    }
    
    // 其他方法直接继承，不需要写！
    // getById、save、updateById、removeById等
}
```

**对比：**
- ❌ **传统方式：** 150+行XML + 50+行Java = 200行
- ✅ **MyBatis-Plus：** 0行XML + 20行Java = 20行

**省去了90%的代码！** 🎉

---

## 📚 学习建议

### 1. 跟着项目学（推荐）⭐

**查看项目中的实现：**
1. 打开 `SysUser.java` - 看注解的使用
2. 打开 `SysUserMapper.java` - 理解 BaseMapper
3. 打开 `SysUserServiceImpl.java` - 学习条件构造
4. 打开 `SysUserController.java` - 看完整流程

**边看边测试接口，理解每一行代码的作用。**

---

### 2. 查看SQL日志（重要）⭐

**每次测试接口后：**
1. 查看控制台的SQL日志
2. 理解 MyBatis-Plus 生成的SQL
3. 对比手写SQL的区别

**例如：**
```java
wrapper.like(SysUser::getUserName, "张");

// 生成的SQL：
WHERE user_name LIKE '%张%'
```

---

### 3. 动手修改代码

**练习1：** 添加按性别查询
```java
// 在 selectUserList 中添加
if (StringUtils.isNotEmpty(user.getSex())) {
    wrapper.eq(SysUser::getSex, user.getSex());
}

// 测试：
GET /system/user/list?sex=0
```

**练习2：** 添加按邮箱模糊查询
```java
if (StringUtils.isNotEmpty(user.getEmail())) {
    wrapper.like(SysUser::getEmail, user.getEmail());
}

// 测试：
GET /system/user/list?email=hrone
```

---

### 4. 理解核心概念

**一定要理解：**

1. **BaseMapper 是什么？**
   - MyBatis-Plus 提供的基础接口
   - 包含常用的CRUD方法
   - 不需要写XML

2. **LambdaQueryWrapper 有什么用？**
   - 类型安全的条件构造器
   - 防止字段名写错
   - 防止SQL注入

3. **IService 和 ServiceImpl 是什么？**
   - Service 层的封装
   - 提供批量操作
   - 简化业务代码

4. **Page 如何工作？**
   - 封装分页参数
   - 自动生成 LIMIT
   - 自动查询总数

---

## ✅ 学习检查清单

### 基础知识 ✅
- [ ] 理解 MyBatis-Plus 的作用
- [ ] 掌握核心注解（@TableName、@TableId、@TableField）
- [ ] 理解 BaseMapper 的概念

### CRUD 操作 ✅
- [ ] 会使用 selectById
- [ ] 会使用 selectList
- [ ] 会使用 insert
- [ ] 会使用 updateById
- [ ] 会使用 deleteById

### 条件构造 ✅
- [ ] 会用 LambdaQueryWrapper
- [ ] 掌握 eq、like、between 等方法
- [ ] 会组合多个条件
- [ ] 会使用 and、or 逻辑

### 分页查询 ✅
- [ ] 会创建 Page 对象
- [ ] 会使用 selectPage
- [ ] 会获取分页信息
- [ ] 理解分页原理

### Service 层 ✅
- [ ] 理解 IService 接口
- [ ] 会使用 ServiceImpl
- [ ] 掌握 save、remove、update等方法
- [ ] 会使用批量操作

---

## 🎯 进阶练习

### 练习1：实现角色管理

**创建 SysRole 实体：**
```java
@TableName("sys_role")
public class SysRole extends BaseEntity {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
    private String roleName;
    private String roleKey;
    private String status;
    // ... 其他字段
}
```

**创建 Mapper、Service、Controller**

**要求：**
- 实现角色的CRUD
- 支持角色名称模糊查询
- 支持分页
- 不能删除系统内置角色

---

### 练习2：实现统计功能

**需求：** 统计不同状态的用户数量

```java
@GetMapping("/stat")
public AjaxResult stat() {
    // 正常用户数
    LambdaQueryWrapper<SysUser> normalWrapper = new LambdaQueryWrapper<>();
    normalWrapper.eq(SysUser::getStatus, "0")
                 .eq(SysUser::getDelFlag, "0");
    Long normalCount = userService.count(normalWrapper);
    
    // 停用用户数
    LambdaQueryWrapper<SysUser> disableWrapper = new LambdaQueryWrapper<>();
    disableWrapper.eq(SysUser::getStatus, "1")
                  .eq(SysUser::getDelFlag, "0");
    Long disableCount = userService.count(disableWrapper);
    
    // 已删除用户数
    LambdaQueryWrapper<SysUser> deletedWrapper = new LambdaQueryWrapper<>();
    deletedWrapper.eq(SysUser::getDelFlag, "2");
    Long deletedCount = userService.count(deletedWrapper);
    
    return AjaxResult.success()
            .put("normalCount", normalCount)
            .put("disableCount", disableCount)
            .put("deletedCount", deletedCount)
            .put("totalCount", normalCount + disableCount);
}
```

---

## 🎊 学习完成！

**恭喜你！** 你已经掌握了 MyBatis-Plus 的核心功能！

**你现在可以：**
- ✅ 使用 MyBatis-Plus 进行开发
- ✅ 不写XML实现CRUD
- ✅ 使用Lambda构建查询条件
- ✅ 实现分页查询
- ✅ 处理业务逻辑

**继续学习：**
- 📖 查看官方文档了解更多高级功能
- 🧪 在项目中多实践
- 💪 尝试实现更复杂的功能

**加油！** 🚀

