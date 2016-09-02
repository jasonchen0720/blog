package com.jason.blog.dao;


import com.jason.blog.pojo.entity.Reply;
import org.springframework.data.repository.Repository;

/**
 * Created by jason on 2016/8/12.
 */
public interface ReplyRepository extends Repository<Reply,Long> {

    Reply save(Reply reply);
    
}
