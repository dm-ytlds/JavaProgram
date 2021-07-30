package com.demi.service.impl;

import com.demi.bean.Book;
import com.demi.dao.BookDao;
import com.demi.dao.impl.BookDaoImpl;
import com.demi.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    // 实例化BookDao对象
    BookDao bookDao = new BookDaoImpl();

    /**
     * 图书查询操作
     * @param book
     * @return
     */
    @Override
    public List<Book> query(Book book) {
        return bookDao.query(book);
    }

    /**
     * 图书添加操作
     * @param book
     */
    @Override
    public void add(Book book) {
        bookDao.add(book);
    }

    /**
     * 图书删除操作
     * @param id
     */
    @Override
    public void delete(int id) {
        bookDao.delete(id);
    }

    /**
     * 修改图书操作
     * @param book
     */
    @Override
    public void update(Book book) {
        bookDao.update(book);
    }
}
