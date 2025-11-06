package com.hrone.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hrone.system.domain.SysUser;

import java.util.List;

/**
 * 用户Service接口
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的IService
 * 2. 自动获得CRUD方法
 * 3. 定义业务方法
 * 
 * 技术要点：
 * - IService<SysUser>：泛型指定实体类
 * - 提供批量操作方法
 * - 支持Lambda查询
 * 
 * IService提供的方法：
 * - save(entity)：保存（插入或更新）
 * - saveBatch(list)：批量保存
 * - removeById(id)：根据ID删除
 * - updateById(entity)：根据ID更新
 * - getById(id)：根据ID查询
 * - list()：查询所有
 * - page(page, wrapper)：分页查询
 * 
 * @author hrone
 */
public interface ISysUserService extends IService<SysUser> {
    
    /**
     * 查询用户列表
     * 
     * @param user 用户信息
     * @return 用户列表
     */
    List<SysUser> selectUserList(SysUser user);
    
    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    SysUser selectUserById(Long userId);
    
    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象
     */
    SysUser selectUserByUserName(String userName);
    
    /**
     * 校验用户名是否唯一
     * 
     * @param userName 用户名
     * @return true=唯一，false=不唯一
     */
    boolean checkUserNameUnique(String userName);
    
    /**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);
    
    /**
     * 修改用户
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);
}

