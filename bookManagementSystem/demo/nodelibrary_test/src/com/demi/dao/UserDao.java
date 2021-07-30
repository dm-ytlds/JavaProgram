package com.demi.dao;

import com.demi.bean.User;

import java.util.List;

public interface UserDao {
    List<User> select();

    // ����û�����
    void add(User user);

    // �޸��û�����
    void update(User user);

    // ɾ���û�����id ����
    void delete(int id);

    // �����û���id����
    void frozen(int id);
}
