package com.demi.dao;

import com.demi.bean.Book;

import java.util.List;

/**
 * 图书管理接口
 */
public interface BookDao {
    // 查询图书信息
    List<Book> query(Book book);
    // 添加图书信息
    void add(Book book);
    // 删除图书信息
    void delete(int id);
    // 修改图书信息
    void update(Book book);

}
