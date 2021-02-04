package com.ww.mall.tiny.service.impl;

import com.ww.mall.tiny.comom.utlis.RedisUtils;
import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;
import com.ww.mall.tiny.service.UmsAdminCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-19 11:31
 * @describe:  权限缓存实现类
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Autowired
    private RedisUtils redisUtils;
    @Value("${redis-admin.database}")
    private String REDIS_DATABASE;
    @Value("${redis-admin.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis-admin.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis-admin.key.permissionList}")
    private String REDIS_KEY_PERMISSION_LIST;


    @Override
    public UmsAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (UmsAdmin) redisUtils.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisUtils.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":" + adminId;
        return (List<UmsPermission>)redisUtils.get(key);
    }

    @Override
    public void setPermissionList(Long adminId, List<UmsPermission> permissionList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSION_LIST + ":" + adminId;
        redisUtils.set(key, permissionList, REDIS_EXPIRE);
    }
}
