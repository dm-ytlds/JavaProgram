package com.demi.dao;


import com.demi.bean.Lend;

import java.util.List;

/**
 * ���Ĺ���ģ�飬���ݳ־û������ӿ�
 */
public interface LendDao {
    // ������ѯ
    List<Lend> query(Lend lend);

    // ��Ӳ���
    void add(Lend lend);

    // ɾ������
    void delete(String id);

    // ���²���
    void update(Lend lend);

}
