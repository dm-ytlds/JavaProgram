package com.demi.dao;

import com.demi.bean.User;

import java.util.List;

public interface UserDao {
    List<User> select();

    void add(User user);

    void update(User user);

    void delete(int id);

    void frozen(int id);
}
