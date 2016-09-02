package com.jason.blog.service.interf;


import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.IssueBo;
import com.jason.blog.pojo.entity.Issue;


import java.util.Map;

/**
 * Created by jason on 2016/8/10.
 */
public interface IssueService {

    IssueBo getIssueList(String column);
    IssueBo getIssue(Long issueId);
    CommonBo saveIssue(Issue issue);
    CommonBo deleteIssue(Long issueId);
}
