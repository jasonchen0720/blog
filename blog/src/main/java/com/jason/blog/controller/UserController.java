package com.jason.blog.controller;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.common.util.MD5Util;
import com.jason.blog.pojo.entity.User;
import com.jason.blog.pojo.form.UserLoginForm;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.pojo.vo.ResponseModel;
import com.jason.blog.pojo.vo.UserVo;
import com.jason.blog.service.interf.UserService;
import com.jason.blog.pojo.form.UserRegisterForm;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jason.blog.common.SysConstant;

/**
 * Created by jason on 2016/8/10.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 进入主页
     */
    @RequestMapping(value = "/toMain")
    public String toMainPage() {
        return "main";
    }

    /**
     * 进入登录页面
     */
    @RequestMapping(value = "/toLogin")
    public String toLogin(HttpServletRequest request) {
        logger.info("进入登录页面");
        //request.getSession().removeAttribute(SysConstant.CURRENT_USER);
        return "user/login";
    }

    /**
     * 进入注册界面
     */
    @RequestMapping(value = "/toRegister")
    public String toRegister(HttpServletRequest request) {
        //request.getSession().removeAttribute(SysConstant.CURRENT_USER);
        return "user/register";
    }

    /**
     * 退出登录
     */
    //配置了logout Filter后。不需要提供此handler
    @RequestMapping(value = "/logout")
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "welcome";
    }

    /**
     * 显示个人信息
     */
    @RequestMapping(value = "/userInfo")
    public String userInfo(Model model, HttpServletRequest request) {
        Subject currentSubject = SecurityUtils.getSubject();
        if (!currentSubject.isAuthenticated()) {
            model.addAttribute("data", new ResponseModel(-1, "未登录或登录超时"));
            return "user/login";
        }
        UserVo userVo = (UserVo)currentSubject.getPrincipal();
        model.addAttribute("user", userVo);
        return "user/userInfo";
    }

    /**
     * 处理登录操作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult, Model model, HttpServletRequest req) {
        if (bindingResult.hasErrors()) {
            logger.error("用户登录参数校验异常:" + bindingResult.getAllErrors().stream().findFirst().toString());
            model.addAttribute("data", ResponseModel.error().message("用户登录表单验证错误"));
            return "user/login";
        }
        User user = userService.getUserByEmail(userLoginForm.getEmail());
        if (user == null) {
            model.addAttribute("data", ResponseModel.error().message(ResultMsg.USER_NOT_FOUND.getMsg()));
            return "user/login";
        }
        Subject currentSubject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userLoginForm.getEmail(), MD5Util.GetMD5Code(userLoginForm.getPassword() + user.getSalt()));
        token.setRememberMe(true);
        try {
            currentSubject.login(token);
            UserVo userVo = (UserVo) currentSubject.getPrincipal();
            logger.info("登录成功:" + userVo);
            //UserVo userVo = (UserVo) currentSubject.getPrincipals().getPrimaryPrincipal();
            req.getSession().setAttribute(SysConstant.CURRENT_USER, userVo);
            //return "main";
            return "redirect:toMain";
        } catch (UnknownAccountException uae) {
            model.addAttribute("data", ResponseModel.error().message(ResultMsg.USER_NOT_FOUND.getMsg()));
        } catch (IncorrectCredentialsException ice) {
            model.addAttribute("data", ResponseModel.error().message(ResultMsg.USER_PASSWORD_ERROR.getMsg()));
        } catch (LockedAccountException lae) {
            model.addAttribute("data", ResponseModel.error().message(ResultMsg.USER_LOCKED.getMsg()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            model.addAttribute("data", ResponseModel.error().message(ResultMsg.USER_LOGIN_ERROR.getMsg()));
            token.clear();
        }
        return "user/login";
    }

    /**
     * 处理注册操作
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseModel register(@Valid UserRegisterForm userRegisterForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("用户注册表单验证异常：" + bindingResult.getAllErrors().stream().findFirst().toString());
            return ResponseModel.error().message("用户注册表单验证错误");
        }
        try {
            CommonBo commonBo = userService.userSave(userRegisterForm.asUser());
            if (commonBo.isSuccess()) {
                logger.info("注册成功");
                return ResponseModel.ok().message(commonBo.getMessage());
            } else {
                logger.error("注册失败");
                return ResponseModel.ok().message(commonBo.getMessage());
            }
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
            return ResponseModel.error().message(e.getErrorMessage());
        } catch (Exception e) {
            return ResponseModel.error().message(e.getMessage());
        }
    }

    /**
     * 邮箱校验
     */
    @ResponseBody
    @RequestMapping(value = "/validateEmail", produces = "application/json;charset=UTF-8")
    public ResponseModel validateUserEmail(@RequestParam(value = "email", defaultValue = "") String email) {
        try {
            if (userService.validateUserEmail(email)) {
                return ResponseModel.ok();
            } else {
                return ResponseModel.error().message("邮箱已经被注册");
            }
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
            return ResponseModel.error().message(e.getErrorMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseModel.error().message(e.getMessage());
        }
    }

    /**
     * 用户名校验
     */
    @ResponseBody
    @RequestMapping(value = "/validateName", produces = "application/json;charset=UTF-8")
    public ResponseModel validateUserName(@RequestParam(value = "username", defaultValue = "") String username) {
        try {
            if (userService.validateUserName(username)) {
                return ResponseModel.ok();
            } else {
                return ResponseModel.error().message("昵称已经被使用");
            }
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
            return ResponseModel.error().message(e.getErrorMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseModel.error().message(e.getMessage());
        }
    }

    @RequestMapping(value = "/test")
    public String test() {
        Subject currentSubject = SecurityUtils.getSubject();
        currentSubject.getPrincipals().getPrimaryPrincipal();
        return null;
    }

}
