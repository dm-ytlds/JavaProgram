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
 * 用户服务类
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    LendDao lendDao = new LendDaoImpl();
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

    /**
     * 查找可借书用户
     * @return
     */
    @Override
    public List<User> selectUserToLend() {
        return userDao.selectUserToLend();
    }

    /**
     * 用户充值
     * @param user
     * @param money
     * @return
     */
    @Override
    public User charge(User user, BigDecimal money) {
        BigDecimal sum = money.add(user.getMoney());
        // 判断充值后余额是否大于0
        if (BigDecimal.ZERO.compareTo(sum) < 0) {
            user.setStatus(Constant.USER_OK);
        }

        user.setMoney(sum);
        // 更改用户文件中的数据
        userDao.update(user);
        // 还需要修改借阅记录中的用户数据
        List<Lend> lendList = lendDao.query(null);
        for (int i = 0; i < lendList.size(); i++) {
            Lend lend = lendList.get(i);
            if (lend.getUser().getId() == user.getId()) {
                // 修改借阅记录中的用户数据
                lend.setUser(user);
                // 更新借阅数据
                lendDao.update(lend);
                break;
            }
        }
        return user;
    }
}
