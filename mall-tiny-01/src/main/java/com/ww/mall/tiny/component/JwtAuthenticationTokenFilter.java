package com.ww.mall.tiny.component;

import com.ww.mall.tiny.comom.utlis.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-15 15:08
 * @describe: 在用户名和密码校验前添加的过滤器，如果请求中有jwt的token且有效，
 *            会取出token中的用户名，然后调用SpringSecurity的API进行登录操作。
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    //内部过滤
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取客户端的authHeader，从中获取token
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());
            String username = jwtTokenUtils.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}", username);
            //将用户的权限set到SecurityContext中 然后在需要的地方才有权限例如（查看品牌接口需要pms:brand:read权限）
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //从数据库中获取信息(这里有一个性能问题，每次访问任何接口都需要访问数据库)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                //判断客户端的token是否有效
                if (jwtTokenUtils.validateToken(authToken, userDetails)) {
                    //如果有效将用户的权限set到SecurityContext中
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
