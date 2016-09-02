package com.jason.blog.service;

import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.exception.BbsErrorEnum;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.IssueBo;
import com.jason.blog.pojo.entity.Issue;
import com.jason.blog.service.interf.IssueService;
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
public class IssueServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    private static Logger logger = Logger.getLogger(IssueServiceTest.class);

    @Autowired
    private IssueService issueService;

    @Test
    public void getIssues() {
        try {
            IssueBo issueBo = issueService.getIssueList("examination");
            Assert.assertTrue(issueBo.getResult());
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIssuesWhenColumnNull() {
        try {
            IssueBo issueBo = issueService.getIssueList("");
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIssue() {
        try {
            IssueBo issueBo = issueService.getIssue(3L);
            Assert.assertTrue(issueBo.getResult());
            logger.info(issueBo.getIssues().get(0).getIssueContent());
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIssueWhenIssueIdNull() {
        try {
            IssueBo issueBo = issueService.getIssue(null);
        } catch (BaseSystemException e) {
            logger.error(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void saveIssue(){
        try{
            Issue issue = new Issue("谁有DSP第四章的作业答案",21L,"jasonchen","examination",null);
            CommonBo commonBo = issueService.saveIssue(issue);
            Assert.assertTrue(commonBo.getResult());
        }catch (BaseSystemException e){
            logger.error(e.getErrorMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void saveIssueWhenIssueNull(){
        try{
            issueService.saveIssue(null);
        }catch (BaseSystemException e){
            logger.error(e.getErrorMessage());
            Assert.assertEquals(e.getErrorMessage(), BbsErrorEnum.BBS_PARAM_NULL.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
