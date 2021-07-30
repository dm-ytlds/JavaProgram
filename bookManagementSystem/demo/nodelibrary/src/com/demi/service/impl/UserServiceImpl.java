package com.demi.service.impl;

import com.demi.bean.User;
import com.demi.dao.UserDao;
import com.demi.dao.impl.UserDaoImpl;
import com.demi.service.UserService;

import java.util.List;

/**
 *  用户服务类
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     *  查询
     * @return
     */
    @Override
    public List<User> select() {
        //调用Dao层写好的方法即可
        return userDao.select();
    }

    /**
     *  添加
     * @param user
     */
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    /**
     *  修改
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     *  删除
     * @param id
     */
    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    /**
     *  冻结
     * @param id
     */
    @Override
    public void frozen(int id) {
        userDao.frozen(id);
    }
}
