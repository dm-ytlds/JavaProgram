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
    // 实例化借阅管理数据持久化层
    private LendDao lendDao = new LendDaoImpl();
    // 实例化用户管理数据持久化层
    private UserDao userDao = new UserDaoImpl();
    // 实例化图书管理数据持久化层
    private BookDao bookDao = new BookDaoImpl();
    @Override
    public List<Lend> query(Lend lend) {
        return lendDao.query(lend);
    }

    /**
     * 添加借阅记录。
     *  根据图书id和用户id来查找出相应的图书和用户即可
     * @param userId
     * @param bookId
     */
    @Override
    public void add(int userId, int bookId) {
        // 根据图书id查找要借的书
        Book book = new Book();
        book.setId(bookId);
        List<Book> bookList = bookDao.query(book);
        // 根据用户id，从可借书的用户中查找要借书的用户
        User user = new User();
        user.setId(userId);
        List<User> userList = userDao.select(user);
        // 实例化借阅对象
        Lend lend = new Lend();
        // 生成借阅编号
        lend.setId(UUID.randomUUID().toString());

        // 赋值图书数据
        Book lendBook = bookList.get(0);
        // 图书的状态设置为出借
        lendBook.setStatus(Constant.STATUS_LEND);
        lend.setBook(lendBook);

        // 赋值用户数据
        User lendUser = userList.get(0);
        // 用户是否已借书的状态设置
        lendUser.setLend(true);
        lend.setUser(lendUser);

        // 借出状态
        lend.setStatus(Constant.LEND_LEND);

        // 借出日期
        LocalDate begin = LocalDate.now();
        lend.setLendDate(begin);
        // 归还日期
        lend.setReturnDate(begin.plusDays(30));

        // 图书数据的修改，需要持久化到文件中
        bookDao.update(lendBook);

        // 用户数据的修改，需要持久化到文件中
        userDao.update(lendUser);

        // 添加借阅记录
        lendDao.add(lend);

    }

    /**
     * 还书操作
     * @param lend
     */
    @Override
    public List<Lend> returnBook(Lend lend) {
        // 获取图书和用户对象
        Book book = lend.getBook();
        User user = lend.getUser();
        // 修改状态
        book.setStatus(Constant.STATUS_STORAGE);
        user.setLend(false);

        userDao.update(user);
        bookDao.update(book);

        // 删除lend数据
        lendDao.delete(lend.getId());

        // 查出当前的并显示
        return lendDao.query(null);
    }

    @Override
    public void update(Lend lend) {
        lendDao.update(lend);
    }
}
