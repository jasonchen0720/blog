package com.jason.blog.service;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.entity.Comment;
import com.jason.blog.pojo.entity.Issue;
import com.jason.blog.service.interf.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by jason on 2016/8/23.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CommentServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    private static Logger logger = Logger.getLogger(CommentServiceTest.class);

    @Autowired
    private CommentService commentService;

    @Test
    public void saveWithoutError(){
        Comment comment = new Comment();
        comment.setCommentContent("你是对的");
        comment.setAuthorId(22L);
        comment.setAuthorName("chenjie");
        Issue issue = new Issue();
        issue.setIssueId(2L);
        comment.setIssue(issue);
        try{
            CommonBo commonBo = commentService.saveComment(comment);
            Assert.assertTrue(commonBo.getResult());
        }catch (BaseSystemException e){
            logger.info(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void saveWhenIssueNotExisted(){
        Comment comment = new Comment();
        comment.setCommentContent("对的");
        comment.setAuthorId(22L);
        comment.setAuthorName("chenjie");
        Issue issue = new Issue();
        issue.setIssueId(1L);
        comment.setIssue(issue);
        try{
            CommonBo commonBo=commentService.saveComment(comment);
            Assert.assertFalse(commonBo.getResult());
            logger.info(commonBo.getMessage());
            Assert.assertEquals(commonBo.getMessage(), ResultMsg.ISSUE_NOT_FOUND.getMsg());
        }catch (BaseSystemException e){
            logger.info(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
