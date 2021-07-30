package com.demi.service.impl;

import com.demi.bean.User;
import com.demi.dao.UserDao;
import com.demi.dao.impl.userDaoImpl;
import com.demi.service.UserService;

import java.util.List;

/**
 * 用户服务类
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new userDaoImpl();
    /**
     * 查询功能
     * @return 查询结果
     */
    @Override
    public List<User> select() {
        // 调用Dao层写好的方法即可。
        // 因为实现的功能是一样的
        return userDao.select();
    }

    /**
     * 用户添加操作
     * @param user
     */
    @Override
    public void add(User user) {
        // 直接调用dao层写好的add()方法，实现添加操作
        userDao.add(user);
    }

    /**
     * 修改操作
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * 删除操作
     * @param id
     */
    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    /**
     * 冻结操作
     * @param id
     */
    @Override
    public void frozen(int id) {
        userDao.frozen(id);
    }
}
