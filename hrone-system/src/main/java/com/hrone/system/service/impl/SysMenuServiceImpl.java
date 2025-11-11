package com.hrone.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrone.common.constant.UserConstants;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.StringUtils;
import com.hrone.common.utils.TreeUtils;
import com.hrone.system.domain.SysMenu;
import com.hrone.system.mapper.SysMenuMapper;
import com.hrone.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单权限 Service实现
 * 
 * @author hrone
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    /**
     * 查询菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotEmpty(menu.getMenuName())) {
            wrapper.like(SysMenu::getMenuName, menu.getMenuName());
        }
        if (StringUtils.isNotEmpty(menu.getVisible())) {
            wrapper.eq(SysMenu::getVisible, menu.getVisible());
        }
        if (StringUtils.isNotEmpty(menu.getStatus())) {
            wrapper.eq(SysMenu::getStatus, menu.getStatus());
        }
        
        wrapper.orderByAsc(SysMenu::getParentId, SysMenu::getOrderNum);
        
        return menuMapper.selectList(wrapper);
    }

    /**
     * 构建菜单树结构
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        return TreeUtils.buildTree(
            menus,
            SysMenu::getMenuId,
            SysMenu::getParentId,
            SysMenu::getChildren,
            SysMenu::setChildren,
            0L
        );
    }

    /**
     * 根据ID查询菜单
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    /**
     * 根据用户ID查询菜单（基于用户-角色-菜单关系）
     */
    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }

    /**
     * 是否存在子菜单
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        return menuMapper.selectCount(wrapper) > 0;
    }

    /**
     * 校验菜单名称是否唯一
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        Long menuId = menu.getMenuId() == null ? -1L : menu.getMenuId();
        
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getMenuName, menu.getMenuName());
        wrapper.eq(SysMenu::getParentId, menu.getParentId());
        
        SysMenu existMenu = menuMapper.selectOne(wrapper);
        
        return existMenu == null || existMenu.getMenuId().equals(menuId);
    }

    /**
     * 新增菜单
     */
    @Override
    public int insertMenu(SysMenu menu) {
        if (!checkMenuNameUnique(menu)) {
            throw new ServiceException("菜单名称已存在");
        }
        
        return menuMapper.insert(menu);
    }

    /**
     * 修改菜单
     */
    @Override
    public int updateMenu(SysMenu menu) {
        if (!checkMenuNameUnique(menu)) {
            throw new ServiceException("菜单名称已存在");
        }
        
        return menuMapper.updateById(menu);
    }

    /**
     * 删除菜单
     */
    @Override
    public int deleteMenuById(Long menuId) {
        if (hasChildByMenuId(menuId)) {
            throw new ServiceException("存在子菜单，不允许删除");
        }
        
        return menuMapper.deleteById(menuId);
    }
}

