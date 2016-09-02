package com.jason.blog.shiro.filter;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Jason on 2016/9/1.
 */
public class RoleAuthorizationFilter extends AuthorizationFilter {
    private static final Logger logger = Logger.getLogger(RoleAuthorizationFilter.class);
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() == null) {
            if (isAjax(request)) {
                //异步请求是会出现无访问权限但是会返回给ajax一个重定向的html，导致无法重定向，且走了ajax的error行方法
                logger.info("RoleAuthorizationFilter-onAccessDenied，登录超时，ajax请求");
                saveRequest(request);
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);//错误码401：访问受限
            } else {
                logger.info("RoleAuthorizationFilter-onAccessDenied，登录超时，非ajax请求");
                saveRequestAndRedirectToLogin(request, response);
            }
        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            String unauthorizedUrl = getUnauthorizedUrl();
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
         Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    private boolean isAjax(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return "XMLHttpRequest".equalsIgnoreCase(httpServletRequest
                .getHeader("X-Requested-With"));
    }
}
