package com.jason.blog.service;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.entity.Comment;
import com.jason.blog.pojo.entity.Reply;
import com.jason.blog.service.interf.ReplyService;
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
public class ReplyServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    private static Logger logger = Logger.getLogger(ReplyServiceTest.class);

    @Autowired
    private ReplyService replyService;

    @Test
    public void saveWithoutError(){
        Reply reply = new Reply();
        reply.setReplyContent("你才是傻逼");
        Comment comment = new Comment();
        comment.setCommentId(2L);
        try{
            CommonBo commonBo = replyService.saveReply(reply);
            Assert.assertTrue(commonBo.getResult());
        }catch (BaseSystemException e){
            logger.info(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void saveWhenCommentNotExisted(){
        Reply reply = new Reply();
        reply.setReplyContent("你才是傻逼");
        Comment comment = new Comment();
        comment.setCommentId(2222L);
        try{
            CommonBo commonBo = replyService.saveReply(reply);
            Assert.assertFalse(commonBo.getResult());
            logger.info(commonBo.getMessage());
            Assert.assertEquals(commonBo.getMessage(), ResultMsg.COMMENT_NOT_FOUND.getMsg());
        }catch (BaseSystemException e){
            logger.info(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
