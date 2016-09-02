package com.jason.blog.controller;

import com.jason.blog.common.SysConstant;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.pojo.form.CommentPublishForm;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.vo.ResponseModel;
import com.jason.blog.pojo.vo.UserVo;
import com.jason.blog.service.interf.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by jason on 2016/8/12.
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController {

    private static Logger log = Logger.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/publish")
    public ResponseModel publishComment(@Valid CommentPublishForm commentPublishForm, BindingResult bindingResult,
                                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error("评论表单验证异常：" + bindingResult.getAllErrors().stream().findFirst().toString());
            return ResponseModel.error().message("发表评论参数异常");
        }
        UserVo author = (UserVo) request.getSession().getAttribute(SysConstant.CURRENT_USER);
        try {
            CommonBo commonBo = commentService.saveComment(commentPublishForm.asComment(author, commentPublishForm.getIssueId()));
            if (commonBo.isSuccess()) {
                return ResponseModel.ok().message(commonBo.getMessage());
            } else {
                log.error(commonBo.getMessage());
                return ResponseModel.error().message(commonBo.getMessage());
            }
        } catch (BaseSystemException e) {
            log.error(e.getErrorMessage());
            return ResponseModel.error().message(e.getErrorMessage());

        } catch (Exception e) {
            return ResponseModel.error().message(e.getMessage());
        }

    }

}
