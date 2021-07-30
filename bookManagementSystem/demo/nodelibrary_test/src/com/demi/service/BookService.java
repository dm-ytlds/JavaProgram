package com.demi.service;

import com.demi.bean.Book;

import java.util.List;

/**
 * 图书管理服务
 */
public interface BookService {
    List<Book> query(Book book);

    void add(Book book);

    void delete(int id);

    void update(Book book);
}
