package com.demi.service;

import com.demi.bean.Book;

import java.util.List;

/**
 * ͼ��������
 */
public interface BookService {
    List<Book> query(Book book);

    void add(Book book);

    void delete(int id);

    void update(Book book);
}
