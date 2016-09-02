package com.jason.blog.service.imp;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.common.util.MD5Util;
import com.jason.blog.dao.UserRepository;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.UserBo;
import com.jason.blog.pojo.entity.User;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.exception.BbsErrorEnum;
import com.jason.blog.service.interf.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jason on 2016/8/10.
 */
@Service
public class UserServiceImp implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;

    private Boolean isEmailOccupied(String email) {
        if (email == null || email.isEmpty())
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        return userRepository.getByEmail(email) != null && userRepository.getByEmail(email).size() > 0;
    }

    private Boolean isUsernameOccupied(String username) {
        if (username == null || username.isEmpty())
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        return userRepository.getByUsername(username) != null && userRepository.getByUsername(username).size() > 0;
    }

    @Transactional
    @Override
    public CommonBo userSave(User user) {
        if (user == null) {
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        }
        if (this.isEmailOccupied(user.getEmail())) {
            throw new BaseSystemException(BbsErrorEnum.BBS_EMAIL_EXISTED);
        }
        if (this.isUsernameOccupied(user.getUsername())) {
            throw new BaseSystemException(BbsErrorEnum.BBS_NAME_EXISTED);
        }
        user = userRepository.save(user);
        if (user != null) {
            return CommonBo.success().message(ResultMsg.USER_REGISTER_OK.getMsg());
        } else {
            return CommonBo.fail().message(ResultMsg.USER_REGISTER_ERROR.getMsg());
        }
    }

    @Transactional
    @Override
    public UserBo userLogin(User user) {
        if (user == null) {
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        }
        List<User> users = userRepository.getByEmail(user.getEmail());
        if (users != null && users.size() == 1) {
            User userQuery = users.get(0);
            log.info(userQuery.getSalt());
            if (MD5Util.GetMD5Code(user.getPassword() + userQuery.getSalt()).equals(userQuery.getPassword())) {
                return (UserBo) UserBo.success(userQuery).message(ResultMsg.USER_LOGIN_OK.getMsg());
            } else {
                return (UserBo) UserBo.fail(userQuery).message(ResultMsg.USER_PASSWORD_ERROR.getMsg());
            }
        } else if (users.size() == 0) {
            log.error("用户不存在");
            return (UserBo) UserBo.fail(null).message(ResultMsg.USER_NOT_FOUND.getMsg());
        } else {
            return (UserBo) UserBo.fail(null).message("系统错误，存在多个用户使用同一邮箱");
        }

    }

    @Transactional
    @Override
    public Boolean validateUserEmail(String email) {
        log.info("验证邮箱："+email);
        if (email == null || email.isEmpty())
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        List<User> users = userRepository.getByEmail(email);
        if (users != null && users.size()>0) {
            log.info("邮箱已经被注册！");
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    @Override
    public Boolean validateUserName(String username) {
        log.info("验证昵称："+username);
        if (username == null || username.isEmpty())
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        List<User> users =  userRepository.getByUsername(username);
        if (users != null && users.size()>0) {
            log.info("昵称已经被使用！");
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.isEmpty())
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        List<User> users = userRepository.getByEmail(email);
        if (users != null && users.size()==1) {
           return users.get(0);
        }
        return null;
    }
}
