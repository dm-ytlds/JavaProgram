package com.demi.dao;

import com.demi.bean.User;

import java.util.List;

public interface UserDao {
    // 用户查询
    List<User> select();
    // 用户查询方法的重载
    List<User> select(User user);
    // 添加用户数据
    void add(User user);

    // 修改用户数据
    void update(User user);

    // 删除用户。用id 即可
    void delete(int id);

    // 冻结用户。id即可
    void frozen(int id);

    List<User> selectUserToLend();
}
