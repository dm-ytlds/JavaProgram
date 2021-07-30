package com.demi.service.impl;

import com.demi.bean.Book;
import com.demi.dao.BookDao;
import com.demi.dao.impl.BookDaoImpl;
import com.demi.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookDao bookDao = new BookDaoImpl();

    /**
     *  ��ѯ
     * @param book
     * @return
     */
    @Override
    public List<Book> select(Book book) {
        return bookDao.select(book);
    }

    /**
     *  ���
     * @param book
     */
    @Override
    public void add(Book book) {
        bookDao.add(book);
    }

    /**
     *  ɾ��
     * @param id
     */
    @Override
    public void delete(int id) {
        bookDao.delete(id);
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }
}
