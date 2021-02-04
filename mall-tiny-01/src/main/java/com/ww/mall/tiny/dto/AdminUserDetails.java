package com.ww.mall.tiny.dto;

import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-15 16:53
 * @describe:   SpringSecurity需要的用户详情（主要是用户信息和权限），需自己实现
 */
public class AdminUserDetails implements UserDetails {

    private UmsAdmin umsAdmin;
    private List<UmsPermission> permissionList;
    public AdminUserDetails(UmsAdmin umsAdmin, List<UmsPermission> permissionList) {
        this.umsAdmin = umsAdmin;
        this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回用户当前权限 list操作看下
        return permissionList.stream()
                .filter(permission -> permission.getValue() != null)    //过滤value值为null
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))   //？？？
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus().equals(1);
    }
}
