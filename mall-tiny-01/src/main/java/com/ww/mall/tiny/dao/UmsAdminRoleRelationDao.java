package com.ww.mall.tiny.dao;

import com.ww.mall.tiny.mbg.mode.UmsPermission;
import org.apache.ibatis.annotations.Param;

import java.security.Permission;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-15 10:47
 * @describe: 管理员（用户）角色dao
 */

public interface UmsAdminRoleRelationDao {

    /**
     * 获取用户所有权限，包括+-权限
     * @param adminId
     * @return
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);

}
