package com.demi.service;

import com.demi.bean.Lend;

import java.util.List;

/**
 * ͼ����ķ���
 */
public interface LendService {
    List<Lend> query(Lend lend);

    // ��ӽ��ļ�¼
    void add(int userId, int bookId);

    // �������
    List<Lend> returnBook(Lend lend);

    // ���²���
    void update(Lend lend);
}
