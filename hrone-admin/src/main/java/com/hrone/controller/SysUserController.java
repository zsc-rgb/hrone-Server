package com.hrone.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hrone.common.annotation.OperLog;
import com.hrone.common.core.controller.BaseController;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.core.page.TableDataInfo;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.enums.BusinessType;
import com.hrone.system.domain.SysUser;
import com.hrone.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户Controller
 * 
 * 功能说明：
 * 1. 用户管理的接口
 * 2. 实现CRUD操作
 * 3. 演示MyBatis-Plus的使用
 * 
 * 技术要点：
 * - 继承BaseController获得通用方法
 * - 使用@Autowired注入Service
 * - 使用@RequestBody接收JSON参数
 * - 使用@PathVariable接收路径参数
 * 
 * @author hrone
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    
    @Autowired
    private ISysUserService userService;
    
    /**
     * 查询用户列表
     * 
     * 访问地址：GET http://localhost:8080/system/user/list
     */
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }
    
    /**
     * 分页查询用户列表
     * 
     * 访问地址：GET http://localhost:8080/system/user/page?pageNum=1&pageSize=10
     * 
     * 参数：
     * - pageNum：页码（默认1）
     * - pageSize：每页大小（默认10）
     */
    @GetMapping("/page")
    public AjaxResult page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysUser user
    ) {
        // 创建分页对象
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        
        // 执行分页查询
        IPage<SysUser> pageResult = userService.page(page);
        
        // 返回分页数据
        return AjaxResult.success("查询成功")
                .put("total", pageResult.getTotal())
                .put("rows", pageResult.getRecords())
                .put("pages", pageResult.getPages())
                .put("current", pageResult.getCurrent())
                .put("size", pageResult.getSize());
    }
    
    /**
     * 根据用户ID查询详细信息
     * 
     * 访问地址：GET http://localhost:8080/system/user/1
     */
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        SysUser user = userService.selectUserById(userId);
        return AjaxResult.success(user);
    }
    
    /**
     * 根据用户名查询用户
     * 
     * 访问地址：GET http://localhost:8080/system/user/username/admin
     */
    @GetMapping("/username/{userName}")
    public AjaxResult getByUserName(@PathVariable String userName) {
        SysUser user = userService.selectUserByUserName(userName);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        return AjaxResult.success(user);
    }
    
    /**
     * 新增用户
     * 
     * 访问地址：POST http://localhost:8080/system/user
     * 
     * 请求体：
     * {
     *   "userName": "test001",
     *   "nickName": "测试用户",
     *   "email": "test@hrone.com",
     *   "phone": "13800138000",
     *   "password": "123456"
     * }
     */
    @OperLog(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user) {
        int rows = userService.insertUser(user);
        return toAjax(rows);
    }
    
    /**
     * 修改用户
     * 
     * 访问地址：PUT http://localhost:8080/system/user
     * 
     * 请求体：
     * {
     *   "userId": 2,
     *   "nickName": "新昵称",
     *   "email": "newemail@hrone.com"
     * }
     */
    @OperLog(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUser user) {
        int rows = userService.updateUser(user);
        return toAjax(rows);
    }
    
    /**
     * 删除用户
     * 
     * 访问地址：DELETE http://localhost:8080/system/user/2
     */
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userId}")
    public AjaxResult remove(@PathVariable Long userId) {
        int rows = userService.deleteUserById(userId);
        return toAjax(rows);
    }
    
    /**
     * 批量删除用户
     * 
     * 访问地址：DELETE http://localhost:8080/system/user/batch
     * 
     * 请求体：
     * {
     *   "userIds": [2, 3, 4]
     * }
     */
    @OperLog(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch")
    public AjaxResult removeBatch(@RequestBody Long[] userIds) {
        int successCount = 0;
        for (Long userId : userIds) {
            try {
                userService.deleteUserById(userId);
                successCount++;
            } catch (ServiceException e) {
                // 跳过失败的，继续删除下一个
                log.warn("删除用户失败，userId={}，原因：{}", userId, e.getMessage());
            }
        }
        
        return AjaxResult.success("成功删除" + successCount + "个用户");
    }
}

