package com.demi.service.impl;

import com.demi.bean.Book;
import com.demi.dao.BookDao;
import com.demi.dao.impl.BookDaoImpl;
import com.demi.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    // ʵ����BookDao����
    BookDao bookDao = new BookDaoImpl();

    /**
     * ͼ���ѯ����
     * @param book
     * @return
     */
    @Override
    public List<Book> query(Book book) {
        return bookDao.query(book);
    }

    /**
     * ͼ����Ӳ���
     * @param book
     */
    @Override
    public void add(Book book) {
        bookDao.add(book);
    }

    /**
     * ͼ��ɾ������
     * @param id
     */
    @Override
    public void delete(int id) {
        bookDao.delete(id);
    }

    /**
     * �޸�ͼ�����
     * @param book
     */
    @Override
    public void update(Book book) {
        bookDao.update(book);
    }
}
