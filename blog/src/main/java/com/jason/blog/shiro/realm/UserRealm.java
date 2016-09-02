package com.jason.blog.shiro.realm;

import com.jason.blog.pojo.entity.User;
import com.jason.blog.pojo.vo.UserVo;
import com.jason.blog.service.interf.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jason on 2016/8/29.
 */
@Service
public class UserRealm extends AuthorizingRealm {

    private static Logger logger = Logger.getLogger(UserRealm.class);

    @Resource
    private UserService userService;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录时输入的用户名
        String email = (String) principalCollection.fromRealm(getName()).iterator().next();
        //到数据库查是否有此对象
        User user = userService.getUserByEmail(email);
        if (user != null) {
            //权限信息对象存放用户的所有角色及权限
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(user.getRolesName());
            //用户的角色对应的所有权限
            user.getRoles().forEach(role -> info.addStringPermissions(role.getPermissionsName()));
            return info;
        }
        return null;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.getUserByEmail(token.getUsername());
        return new SimpleAuthenticationInfo(new UserVo(user), user.getPassword(), getName());
    }
}
