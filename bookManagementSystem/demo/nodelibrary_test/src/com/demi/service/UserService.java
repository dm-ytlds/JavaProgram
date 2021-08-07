package com.demi.service;

import com.demi.bean.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    List<User> select();
    void add(User user);
    void update(User user);
    void delete(int id);
    void frozen(int id);
    List<User> selectUserToLend();
    // ³äÖµ²Ù×÷
    User charge(User user, BigDecimal money);
}
