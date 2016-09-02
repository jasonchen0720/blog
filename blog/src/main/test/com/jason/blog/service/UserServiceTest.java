package com.jason.blog.service;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.exception.BbsErrorEnum;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.UserBo;
import com.jason.blog.pojo.entity.User;
import com.jason.blog.service.interf.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jason on 2016/8/23.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest extends AbstractTestNGSpringContextTests {

    private static Logger logger = Logger.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void saveWhenUserIsNull() {
        User user = null;
        try {
            userService.userSave(user);
        } catch (BaseSystemException e) {
            logger.info(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        }
    }

    @Test
    public void saveWhenEmailIsExisted() {
        User user = new User("111@qq.com", "testname", "password");
        try {
            userService.userSave(user);
        } catch (BaseSystemException e) {
            logger.info(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_EMAIL_EXISTED.getMessage());
        }
    }

    @Test
    public void saveWhenEmailIsEmpty() {
        User user = new User("", "testname", "password");
        try {
            userService.userSave(user);
        } catch (BaseSystemException e) {
            logger.info(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        }
    }

    @Test
    public void saveWhenUsernameIsExisted() {
        User user = new User("121132323@qq.com", "jasonchen", "password");
        try {
            userService.userSave(user);
        } catch (BaseSystemException e) {
            logger.info(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_NAME_EXISTED.getMessage());
        }
    }

    @Test
    public void saveWhenUsernameIsEmpty() {
        User user = new User("121132323@qq.com", "", "password");
        try {
            userService.userSave(user);
        } catch (BaseSystemException e) {
            logger.info(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        }
    }

    @Test
    public void saveWithoutError() {
        User user = new User("1211test@qq.com", "testname123", "testpassword");
        try {
            CommonBo commonBo = userService.userSave(user);
            Assert.assertTrue(commonBo.getResult());
        } catch (Exception e) {
            logger.error("保存用户信息测试失败");
        }
    }

    @Test
    public void loginWithoutError() {
        User user = new User("111@qq.com", "", "chenjie123");
        try {
            UserBo userBo = userService.userLogin(user);
            Assert.assertTrue(userBo.getResult());
        } catch (Exception e) {
            logger.error("登录测试失败");
        }

    }

    @Test
    public void loginWhenPasswordError() {
        User user = new User("111@qq.com", "", "chenjie23");
        try {
            UserBo userBo = userService.userLogin(user);
            Assert.assertFalse(userBo.getResult());
            logger.info(userBo.getMessage());
            Assert.assertEquals(userBo.getMessage(), ResultMsg.USER_PASSWORD_ERROR.getMsg());
        } catch (Exception e) {
            logger.error("登录测试失败");
        }

    }

    @Test
    public void loginWhenEmailError() {
        User user = new User("1112332221112@qq.com", "", "chenjie23");
        try {
            UserBo userBo = userService.userLogin(user);
            Assert.assertFalse(userBo.getResult());
            logger.info(userBo.getMessage());
            Assert.assertEquals(userBo.getMessage(), ResultMsg.USER_NOT_FOUND.getMsg());
        } catch (Exception e) {
            logger.error("测试失败");
        }

    }

    @Test
    public void validateEmailWhenExisted() {
        try {
            Boolean r = userService.validateUserEmail("111@qq.com");
            Assert.assertFalse(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateEmailWhenValid() {
        try {
            Boolean r = userService.validateUserEmail("111testtest@qq.com");
            Assert.assertTrue(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateUsernameWhenExisted() {
        try {
            Boolean r = userService.validateUserName("jasonchen");
            Assert.assertFalse(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateUsernameWhenValid() {
        try {
            Boolean r = userService.validateUserName("jasonchen1test");
            Assert.assertTrue(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



