package com.ww.mall.tiny.component;

import cn.hutool.json.JSONUtil;
import com.ww.mall.tiny.comom.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-15 15:38
 * @describe:   当访问接口没有权限，自定义返回结果
 */

@Component
public class RestFulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        httpServletResponse.getWriter().flush();
    }
}
