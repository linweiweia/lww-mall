package com.ww.mall.tiny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.ww.mall.tiny.comom.utlis.JwtTokenUtils;
import com.ww.mall.tiny.dao.UmsAdminRoleRelationDao;
import com.ww.mall.tiny.mbg.mapper.UmsAdminMapper;
import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsAdminExample;
import com.ww.mall.tiny.mbg.mode.UmsPermission;
import com.ww.mall.tiny.service.UmsAdminCacheService;
import com.ww.mall.tiny.service.UmsAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.Date;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-14 17:55
 * @describe: 后台管理员service
 */

@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminService.class);


    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    @Override
    public UmsAdmin register(UmsAdmin umsAdminPara) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminPara, umsAdmin);
        umsAdmin.setStatus(1);
        umsAdmin.setCreateTime(new Date());
        //判断该用户名是否已被注册
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密处理然后在存入数据库
        //passwordEncoder是SpringSecurity包的接口默认的encode就是采用BCryptPasswordEncoder
        String encodePassWord = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassWord);
        //存入数据库
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            //根据用户名称，去数据库中查询并且封装成userDetails
            //只是这个userDetailsService是在security里面自己定义public UserDetailsService userDetailsService()
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //判断密码是否正确
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw  new BadCredentialsException("密码不正确");
            }
            //如果验证通过就把当前用户（参数userDetails）的权限set到SecurityContext中，就是相当于把当前用户权限存起来。
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //返回token
            token = jwtTokenUtils.generate(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登入异常:{}",e.getMessage());
        }
        return token;
    }



    //改造先从缓存中取数
    @Override
    public List<UmsPermission> getPermissionsList(Long adminId) {
        //先从缓存中取用户权限
        List<UmsPermission> permissionList = adminCacheService.getPermissionList(adminId);
        if (CollUtil.isNotEmpty(permissionList)) {
            LOGGER.info("get permissionData from redis:{}", permissionList);
            return permissionList;
        }
        //缓存中没有从数据中取
        permissionList = umsAdminRoleRelationDao.getPermissionList(adminId);
        if (CollUtil.isNotEmpty(permissionList)) {
            //存入redis中
            adminCacheService.setPermissionList(adminId, permissionList);
        }
        return permissionList;
    }

    //改造先从缓存中取数
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        //先从redis中获取用户信息
        UmsAdmin admin = adminCacheService.getAdmin(username);
        if (admin != null) {
            LOGGER.info("get adminData from redis:{}", admin);
            return admin;
        }
        //redis中没有在从数据库中取
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            //存入redis
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

}
