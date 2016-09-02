package com.jason.blog.service.interf;


import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.UserBo;
import com.jason.blog.pojo.entity.User;


/**
 * Created by jason on 2016/8/10.
 */
public interface UserService {

    CommonBo userSave(User user);

    UserBo userLogin(User user);

    Boolean validateUserEmail(String email);

    Boolean validateUserName(String username);

    User getUserByEmail(String email);
}
