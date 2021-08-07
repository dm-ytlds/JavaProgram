package com.demi.service.impl;

import com.demi.bean.Constant;
import com.demi.bean.Lend;
import com.demi.bean.User;
import com.demi.dao.LendDao;
import com.demi.dao.UserDao;
import com.demi.dao.impl.LendDaoImpl;
import com.demi.dao.impl.UserDaoImpl;
import com.demi.service.UserService;

import java.math.BigDecimal;
import java.util.List;

/**
 * �û�������
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    LendDao lendDao = new LendDaoImpl();
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

    /**
     * ���ҿɽ����û�
     * @return
     */
    @Override
    public List<User> selectUserToLend() {
        return userDao.selectUserToLend();
    }

    /**
     * �û���ֵ
     * @param user
     * @param money
     * @return
     */
    @Override
    public User charge(User user, BigDecimal money) {
        BigDecimal sum = money.add(user.getMoney());
        // �жϳ�ֵ������Ƿ����0
        if (BigDecimal.ZERO.compareTo(sum) < 0) {
            user.setStatus(Constant.USER_OK);
        }

        user.setMoney(sum);
        // �����û��ļ��е�����
        userDao.update(user);
        // ����Ҫ�޸Ľ��ļ�¼�е��û�����
        List<Lend> lendList = lendDao.query(null);
        for (int i = 0; i < lendList.size(); i++) {
            Lend lend = lendList.get(i);
            if (lend.getUser().getId() == user.getId()) {
                // �޸Ľ��ļ�¼�е��û�����
                lend.setUser(user);
                // ���½�������
                lendDao.update(lend);
                break;
            }
        }
        return user;
    }
}
