package com.jason.blog.dao;


import com.jason.blog.pojo.entity.Issue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jason on 2016/8/12.
 */
public interface IssueRepository extends CrudRepository<Issue,Long> {

    List<Issue> getByColumnBelong(String columnBelong);

}