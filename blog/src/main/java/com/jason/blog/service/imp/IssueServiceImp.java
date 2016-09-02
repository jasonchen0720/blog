package com.jason.blog.service.imp;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.dao.IssueRepository;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.exception.BbsErrorEnum;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.bo.IssueBo;
import com.jason.blog.pojo.entity.Comment;
import com.jason.blog.pojo.entity.Issue;
import com.jason.blog.service.interf.IssueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/8/11.
 */
@Service
public class IssueServiceImp implements IssueService {

    private static Logger log = Logger.getLogger(IssueServiceImp.class);

    @Autowired
    private IssueRepository issueRepository;

    @Transactional
    @Override
    public IssueBo getIssueList(String column) {
        if (column == null || column.isEmpty()) {
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        }
        List<Issue> issues = issueRepository.getByColumnBelong(column);
        if (issues != null) {
            log.info("获取主题列表成功");
            return (IssueBo) IssueBo.success(issues).message(ResultMsg.ISSUE_LOAD_OK.getMsg());
        } else {
            return (IssueBo) IssueBo.success(issues).message(ResultMsg.ISSUE_LOAD_ERROR.getMsg());
        }
    }

    @Transactional
    @Override
    public IssueBo getIssue(Long issueId) {
        if (issueId == null){
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        }
        Issue issue = issueRepository.findOne(issueId);
        if (issue == null) {
            return (IssueBo) IssueBo.fail(null).message(ResultMsg.ISSUE_NOT_FOUND.getMsg());
        } else {
            log.info("获取到帖子细节");
            List<Comment> commentList = issue.getComments();
            commentList.forEach(comment -> comment.getReplies());
            List<Issue> issues = new ArrayList<>();
            issues.add(issue);
            return (IssueBo) IssueBo.success(issues).message(ResultMsg.ISSUE_GET_OK.getMsg());
        }
    }

    @Transactional
    @Override
    public CommonBo saveIssue(Issue issue) {
        if (issue == null)
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        issue = issueRepository.save(issue);
        if (issue != null) {
            return CommonBo.success().message(ResultMsg.ISSUE_SAVE_OK.getMsg());
        } else {
            return CommonBo.fail().message(ResultMsg.ISSUE_SAVE_ERROR.getMsg());
        }
    }

    @Transactional
    @Override
    public CommonBo deleteIssue(Long issueId){
        if(issueId==null){
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        }
        if(issueRepository.exists(issueId)){
            issueRepository.delete(issueId);
            return CommonBo.success().message(ResultMsg.ISSUE_DELETE_OK.getMsg());
        }else{
            return CommonBo.fail().message(ResultMsg.ISSUE_DELETE_ERROR.getMsg());
        }
    }
}
