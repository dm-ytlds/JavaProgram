package com.demi.dao;

import com.demi.bean.User;

import java.util.List;

public interface UserDao {
    // �û���ѯ
    List<User> select();
    // �û���ѯ����������
    List<User> select(User user);
    // ����û�����
    void add(User user);

    // �޸��û�����
    void update(User user);

    // ɾ���û�����id ����
    void delete(int id);

    // �����û���id����
    void frozen(int id);

    List<User> selectUserToLend();
}
