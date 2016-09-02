package com.jason.blog.service.interf;

import com.jason.blog.pojo.bo.CommonBo;
import com.jason.blog.pojo.entity.Reply;

import java.util.Map;

/**
 * Created by jason on 2016/8/10.
 */
public interface ReplyService {

    CommonBo saveReply(Reply reply);

}
