package com.demi.service.impl;

import com.demi.bean.Book;
import com.demi.bean.Constant;
import com.demi.bean.Lend;
import com.demi.bean.User;
import com.demi.dao.BookDao;
import com.demi.dao.LendDao;
import com.demi.dao.UserDao;
import com.demi.dao.impl.BookDaoImpl;
import com.demi.dao.impl.LendDaoImpl;
import com.demi.dao.impl.UserDaoImpl;
import com.demi.service.LendService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class LendServiceImpl implements LendService {
    // ʵ�������Ĺ������ݳ־û���
    private LendDao lendDao = new LendDaoImpl();
    // ʵ�����û��������ݳ־û���
    private UserDao userDao = new UserDaoImpl();
    // ʵ����ͼ��������ݳ־û���
    private BookDao bookDao = new BookDaoImpl();
    @Override
    public List<Lend> query(Lend lend) {
        return lendDao.query(lend);
    }

    /**
     * ��ӽ��ļ�¼��
     *  ����ͼ��id���û�id�����ҳ���Ӧ��ͼ����û�����
     * @param userId
     * @param bookId
     */
    @Override
    public void add(int userId, int bookId) {
        // ����ͼ��id����Ҫ�����
        Book book = new Book();
        book.setId(bookId);
        List<Book> bookList = bookDao.query(book);
        // �����û�id���ӿɽ�����û��в���Ҫ������û�
        User user = new User();
        user.setId(userId);
        List<User> userList = userDao.select(user);
        // ʵ�������Ķ���
        Lend lend = new Lend();
        // ���ɽ��ı��
        lend.setId(UUID.randomUUID().toString());

        // ��ֵͼ������
        Book lendBook = bookList.get(0);
        // ͼ���״̬����Ϊ����
        lendBook.setStatus(Constant.STATUS_LEND);
        lend.setBook(lendBook);

        // ��ֵ�û�����
        User lendUser = userList.get(0);
        // �û��Ƿ��ѽ����״̬����
        lendUser.setLend(true);
        lend.setUser(lendUser);

        // ���״̬
        lend.setStatus(Constant.LEND_LEND);

        // �������
        LocalDate begin = LocalDate.now();
        lend.setLendDate(begin);
        // �黹����
        lend.setReturnDate(begin.plusDays(30));

        // ͼ�����ݵ��޸ģ���Ҫ�־û����ļ���
        bookDao.update(lendBook);

        // �û����ݵ��޸ģ���Ҫ�־û����ļ���
        userDao.update(lendUser);

        // ��ӽ��ļ�¼
        lendDao.add(lend);

    }

    /**
     * �������
     * @param lend
     */
    @Override
    public List<Lend> returnBook(Lend lend) {
        // ��ȡͼ����û�����
        Book book = lend.getBook();
        User user = lend.getUser();
        // �޸�״̬
        book.setStatus(Constant.STATUS_STORAGE);
        user.setLend(false);

        userDao.update(user);
        bookDao.update(book);

        // ɾ��lend����
        lendDao.delete(lend.getId());

        // �����ǰ�Ĳ���ʾ
        return lendDao.query(null);
    }

    @Override
    public void update(Lend lend) {
        lendDao.update(lend);
    }
}
