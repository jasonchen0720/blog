package com.jason.blog.dao;

import com.jason.blog.pojo.entity.Comment;
import org.springframework.data.repository.Repository;

/**
 * Created by jason on 2016/8/12.
 */
public interface CommentRepository extends Repository<Comment,Long> {

    Comment save(Comment comment);

    Comment getByCommentId(Long commentId);
}
