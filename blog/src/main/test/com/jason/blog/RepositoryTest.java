package com.jason.blog;


import com.jason.blog.dao.IssueRepository;
import com.jason.blog.dao.UserRepository;
import com.jason.blog.pojo.entity.Issue;
import com.jason.blog.pojo.entity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


/**
 * Created by jason on 2016/8/11.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RepositoryTest extends AbstractTestNGSpringContextTests {

    private static Logger logger = Logger.getLogger(RepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Test
    public void testSaveUser() {

        User user = userRepository.save(new User("111@qq.com", "jasonchen", "1"));

        Assert.assertNotNull(user);

        logger.info(user.getUserId());

    }

    @Test
    public void testGetUserByEmail() {

        List<User> users = userRepository.getByEmail("1132321@qq.com");

        logger.info(users.size());

        Assert.assertEquals(users.size(), 1);

        logger.info(users.get(0).getUsername());
    }

    @Test
    public void testGetUserByUsername() {

        List<User> users = userRepository.getByUsername("jasonchen");

        logger.info(users.size());

        Assert.assertEquals(users.size(), 1);

        logger.info(users.get(0).getUserId());

    }

    @Test
    public void testSaveIssue(){

        Issue issue = issueRepository.save(new Issue("考试了",21L,"jasonchen","examination",null));

        Assert.assertNotNull(issue);

        logger.info(issue.getIssueId());

    }

    @Test
    public void testGetIssueByColumnBelong(){

        List<Issue> issues = issueRepository.getByColumnBelong("examination");

        logger.info(issues.size());

        if(issues.size()> 0){

            issues.forEach(issue -> issue.getIssueContent());

        }
    }

}
