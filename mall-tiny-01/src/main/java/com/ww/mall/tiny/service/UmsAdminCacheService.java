package com.ww.mall.tiny.service;

import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;

import java.security.Permission;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-19 11:20
 * @describe:   权限模块 缓存操作
 */
public interface UmsAdminCacheService {

    /**
     * 获取用户信息
     */
    UmsAdmin getAdmin(String username);

    /**
     * 设置用户信息
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取用户权限列表
     */
    List<UmsPermission> getPermissionList(Long adminId);

    /**
     * 设置用户权限列表
     */
    void setPermissionList(Long adminId, List<UmsPermission> permissionList);


    /**
     * 删除用户缓存信息
     */

    /**
     * 删除用户权限缓存信息
     */


    /**
     * 删除
     */
}
