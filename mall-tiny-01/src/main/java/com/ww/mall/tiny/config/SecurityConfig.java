package com.ww.mall.tiny.config;

import com.ww.mall.tiny.component.JwtAuthenticationTokenFilter;
import com.ww.mall.tiny.component.RestAuthenticationEntryPoint;
import com.ww.mall.tiny.component.RestFulAccessDeniedHandler;
import com.ww.mall.tiny.dto.AdminUserDetails;
import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;
import com.ww.mall.tiny.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-14 17:24
 * @describe:   SpringSecurity配置类
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启方法权限验证，访问controller的接口需要验证？？？
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RestFulAccessDeniedHandler restFulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /**
     * 1.设置拦截哪些资源
     * 2.设置过滤器jwt的校验
     * 3.未登入或者权限不足等处理方式
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()   //因为使用jwt不是session不用考虑跨域问题
                .sessionManagement()    //没有使用session不保存session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,    //必要静态资源可以无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll()
                .antMatchers("/admin/login","/admin/register")  //登入和注册也可以访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)    //可以探测本服务支持的请求方式
                .permitAll()
//                .antMatchers("/**")//测试时全部运行访问
//                .permitAll()
                .anyRequest()   //除上面之外所有请求都要鉴权认证
                .authenticated();
        //禁用缓存
        httpSecurity.headers().cacheControl();
        //设置一个jwt过滤器在密码校验前执行
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //设置访问接口权限不足返回自定义处理，未登入返回自定义处理结果
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restFulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    /**
     * jwt过滤器，用于在密码验证之前执行。如果token校验通过是直接登，再交给SpringSecurity做后续处理？
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }


    /**
     * 主要设置密码相关等
     * AuthenticationManagerBuilder > AuthenticationManagerProvide
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 关键接口：用于根据用户名获取用户信息，需要自行实现
     * 就是前段传数据过来，获取客户端的username，然后根据username去数据库查询用户信息
     * configure方法用到  UmsAdminService中也用到了
     */
    @Bean
    public UserDetailsService userDetailsService() {
        //从数据库获取用户信息，封装成SpringSecurity需要的UserDetailsService
        return username -> {
            UmsAdmin admin = adminService.getAdminByUsername(username);
            if (admin != null) {
                List<UmsPermission> permissionsList = adminService.getPermissionsList(admin.getId());
                //AdminUserDetails自己实现了UserDetails接口
                return new AdminUserDetails(admin, permissionsList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    /**
     * 设置密码加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
