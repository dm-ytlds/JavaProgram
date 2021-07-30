package com.demi.service.impl;

import com.demi.bean.User;
import com.demi.dao.UserDao;
import com.demi.dao.impl.userDaoImpl;
import com.demi.service.UserService;

import java.util.List;

/**
 * �û�������
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new userDaoImpl();
    /**
     * ��ѯ����
     * @return ��ѯ���
     */
    @Override
    public List<User> select() {
        // ����Dao��д�õķ������ɡ�
        // ��Ϊʵ�ֵĹ�����һ����
        return userDao.select();
    }

    /**
     * �û���Ӳ���
     * @param user
     */
    @Override
    public void add(User user) {
        // ֱ�ӵ���dao��д�õ�add()������ʵ����Ӳ���
        userDao.add(user);
    }

    /**
     * �޸Ĳ���
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * ɾ������
     * @param id
     */
    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    /**
     * �������
     * @param id
     */
    @Override
    public void frozen(int id) {
        userDao.frozen(id);
    }
}
