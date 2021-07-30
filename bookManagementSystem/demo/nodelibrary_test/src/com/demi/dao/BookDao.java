package com.demi.dao;

import com.demi.bean.Book;

import java.util.List;

/**
 * ͼ�����ӿ�
 */
public interface BookDao {
    // ��ѯͼ����Ϣ
    List<Book> query(Book book);
    // ���ͼ����Ϣ
    void add(Book book);
    // ɾ��ͼ����Ϣ
    void delete(int id);
    // �޸�ͼ����Ϣ
    void update(Book book);

}
