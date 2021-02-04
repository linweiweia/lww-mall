package com.ww.mall.tiny.service;

import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-14 17:51
 * @describe:   后台管理员(用户)service
 */

public interface UmsAdminService {

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdmin umsAdminPara);

    /**
     * 登入
     * @param username  用户名
     * @param password  密码
     * @return 返回token
     */
    String login(String username, String password);

    /**
     * 更具用户名称获取用户信息
     */
    UmsAdmin getAdminByUsername(String username);


    /**
     * 获取用户所有权限包括+-的权限
     */
    List<UmsPermission> getPermissionsList(Long adminID);
}
