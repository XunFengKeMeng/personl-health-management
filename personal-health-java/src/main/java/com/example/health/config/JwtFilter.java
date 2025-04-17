package com.example.health.config;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huanghaiming
 * @date 2025/4/17 23:58
 * @description:
 */
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 预处理跨域
     *
     * @param request 请求
     * @param response 响应
     * @return 继续执行过滤器链或（false）中断当前请求处理
     * @throws Exception 异常
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 处理跨域
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:21091");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
            "Content-Type,Access-Control-Allow-Headers,Authorization,X-Requested-With");

        // 放行OPTIONS请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}