package com.jason.blog.dao;

import com.jason.blog.pojo.entity.User;
import org.springframework.data.repository.Repository;
import java.util.List;

/**
 * Created by jason on 2016/8/11.
 */
public interface UserRepository extends Repository<User,Long > {

    User save(User user);

    List<User> getByEmail(String email);

    List<User> getByUsername(String username);

    User getByUserId(Long userId);

    User getByEmailAndPassword(String email, String password);

}
