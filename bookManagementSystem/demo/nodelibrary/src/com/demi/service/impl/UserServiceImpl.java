package com.demi.service.impl;

import com.demi.bean.User;
import com.demi.dao.UserDao;
import com.demi.dao.impl.UserDaoImpl;
import com.demi.service.UserService;

import java.util.List;

/**
 *  �û�������
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     *  ��ѯ
     * @return
     */
    @Override
    public List<User> select() {
        //����Dao��д�õķ�������
        return userDao.select();
    }

    /**
     *  ���
     * @param user
     */
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    /**
     *  �޸�
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     *  ɾ��
     * @param id
     */
    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    /**
     *  ����
     * @param id
     */
    @Override
    public void frozen(int id) {
        userDao.frozen(id);
    }
}
