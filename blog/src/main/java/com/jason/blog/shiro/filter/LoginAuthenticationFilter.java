package com.jason.blog.shiro.filter;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jason on 2016/9/1.
 */
public class LoginAuthenticationFilter extends AuthenticationFilter {
    private static final Logger logger = Logger.getLogger(LoginAuthenticationFilter.class);
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated()) {
            if (isAjax(request)) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
                try {
                    httpServletResponse.getWriter().write("{code:-1,message:" +"'您还未登录或者登录超时'"+ "}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                logger.info("RoleAuthorizationFilter-onAccessDenied，登录超时，非ajax请求");
                saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }else{
            return true;
        }
    }
    private boolean isAjax(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return "XMLHttpRequest".equalsIgnoreCase(httpServletRequest
                .getHeader("X-Requested-With"));
    }
}
