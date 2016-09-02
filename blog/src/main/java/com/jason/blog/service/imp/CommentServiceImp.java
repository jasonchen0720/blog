package com.jason.blog.service.imp;

import com.jason.blog.common.SysEnum.ResultMsg;
import com.jason.blog.dao.CommentRepository;
import com.jason.blog.dao.IssueRepository;
import com.jason.blog.exception.BaseSystemException;
import com.jason.blog.exception.BbsErrorEnum;
import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.entity.Comment;
import com.jason.blog.pojo.entity.Issue;
import com.jason.blog.service.interf.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jason on 2016/8/12.
 */
@Service
public class CommentServiceImp implements CommentService {

    private static Logger log = Logger.getLogger(CommentServiceImp.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Transactional
    @Override
    public CommonBo saveComment(Comment comment) {
        if (comment == null || comment.getIssue() == null || comment.getIssue().getIssueId() == null)
            throw new BaseSystemException(BbsErrorEnum.BBS_PARAM_NULL);
        Issue issue = issueRepository.findOne(comment.getIssue().getIssueId());
        if (issue != null) {
            comment.setIssue(issue);
            comment = commentRepository.save(comment);
            if (comment != null) {
                log.info("保存评论成功");
                return CommonBo.success().message(ResultMsg.COMMENT_SAVE_OK.getMsg());
            } else {
                return CommonBo.fail().message(ResultMsg.COMMENT_SAVE_ERROR.getMsg());
            }
        } else {
            log.error("评论的帖子不存在");
            return CommonBo.fail().message(ResultMsg.ISSUE_NOT_FOUND.getMsg());
        }
    }

    /*@Transactional
    @Override
    public Map<String, Object> getComment(Long commentId) {
        Map<String, Object> map = new HashMap<>();
        Comment comment = (Comment) commentDao.get(Comment.class, commentId);
        if (comment == null) {
            log.info("评论被删除或评论不存在");
            map.put(SysConstant.RESP_CODE, SysEnum.ResultCode.ERROR.getCode());
            map.put(SysConstant.RESP_MSG, SysEnum.ResultMsg.COMMENT_NOT_FOUND.getMsg());
        } else {
            map.put(SysConstant.RESP_DATA, comment);
            map.put(SysConstant.RESP_CODE, SysEnum.ResultCode.OK.getCode());
        }
        return map;
    }*/
}
