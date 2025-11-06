package com.hrone.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrone.system.domain.SysUser;

/**
 * 用户Mapper接口
 * 
 * 功能说明：
 * 1. 继承MyBatis-Plus的BaseMapper
 * 2. 自动获得增删改查方法
 * 3. 可以添加自定义SQL方法
 * 
 * 技术要点：
 * - BaseMapper<SysUser>：泛型指定实体类
 * - 自动注入常用CRUD方法
 * - 支持Lambda查询
 * 
 * BaseMapper提供的方法：
 * - insert(entity)：插入
 * - deleteById(id)：根据ID删除
 * - updateById(entity)：根据ID更新
 * - selectById(id)：根据ID查询
 * - selectList(wrapper)：条件查询列表
 * - selectPage(page, wrapper)：分页查询
 * 
 * @author hrone
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    // BaseMapper已经提供了常用的CRUD方法
    // 这里可以添加自定义的SQL方法
    
    /**
     * 示例：自定义查询方法
     * 可以在对应的XML文件中编写SQL
     * 
     * 例如：根据用户名查询用户
     * SysUser selectUserByUserName(String userName);
     */
}

