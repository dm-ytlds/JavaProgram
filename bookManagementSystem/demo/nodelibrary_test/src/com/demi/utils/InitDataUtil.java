package com.demi.utils;

import com.demi.bean.*;
import com.demi.dao.BookDao;
import com.demi.dao.LendDao;
import com.demi.dao.impl.BookDaoImpl;
import com.demi.dao.impl.LendDaoImpl;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
    用户数据管理
 */
public class InitDataUtil {
    public static void main(String[] args) {
        // test
        // 初始化用户数据
        List<User> userList = new ArrayList<>();
        userList.add(new User(2021001, "demi", Constant.USER_OK, new BigDecimal(100), false));
        userList.add(new User(2021002, "lee", Constant.USER_FROZEN, BigDecimal.TEN, false));
        userList.add(new User(2021003, "john", Constant.USER_OK, BigDecimal.ZERO, false));
        initData(PathConstant.USER_PATH, userList);

        // 初始化图书数据
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(01, "Java基础教程", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE));
        bookList.add(new Book(02, "Java进阶教程", "ddemi", Constant.TYPE_COMPUTER, "001-002", "qingHua University", Constant.STATUS_STORAGE));
        initData(PathConstant.BOOK_PATH, bookList);

        // 初始化图书借阅数据
        List<Lend> lendList = new ArrayList<>();
        // 借阅人
        User user = new User(1001, "demi", Constant.USER_OK, new BigDecimal(100), false);
        // 借阅书籍
        Book book = new Book(01, "Java基础教程", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE);
        Lend lend = new Lend();
        // 使用UUID生成借阅编号
        lend.setId(UUID.randomUUID().toString());
        lend.setBook(book);
        lend.setUser(user);
        lend.setStatus(Constant.LEND_LEND);
        // 更新图书管理文件中的图书状态
        BookDao bookDao = new BookDaoImpl();
        book.setStatus(Constant.STATUS_LEND);
        bookDao.update(book);
        // 出借日期
        LocalDate begin = LocalDate.now();
        lend.setLendDate(begin);
        // 归还日期
        lend.setReturnDate(begin.plusDays(30));
        // 将借阅信息放入集合中
        lendList.add(lend);
        initData(PathConstant.LEND_PATH, lendList);
    }

   /* *//**
     * 初始化用户数据
     * @param userList
     *//*
    public static void initUser(List<User> userList) {
        // 为了方便流的关闭，将输出对象流放在外面
        ObjectOutputStream oos = null;
        try {
            // 创建文件夹和文件
            File directory = new File("user/");
            // 防止出错以及重复写，命名一个常量直接调用
            File file = new File(PathConstant.USER_PATH);
            // 判断文件夹是否存在
            if (!directory.exists()) {
                directory.mkdir();
            }
            // 判断文件是否存在
            if (!file.exists()) {
                file.createNewFile();
                List<User> users = new ArrayList<>();
                // 如果文件中没有数据，可以初始化一些数据
                if (userList == null) {
                    // 常量代替状态值
                    users.add(new User(1001, "demi", Constant.USER_OK, new BigDecimal(100)));
                    users.add(new User(1002, "lee", Constant.USER_FROZEN, BigDecimal.TEN));
                } else {
                    users = userList;
                }
                // 利用对象输出流将users拷贝到文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                // 将集合中的数据传入oos流中
                oos.writeObject(users);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    *//**
     * 初始化图书信息
     * @param bookList
     *//*
    private static void initBook(List<Book> bookList) {

        ObjectOutputStream oos = null;
        // 初始化文件目录及其文件
        File directory = new File("book/");
        File file = new File(PathConstant.BOOK_PATH);
        // 判断文件夹是否存在
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                // 文件不存在，刚创建的文件也必然没有数据
                // 初始化几条图书信息
                List<Book> books = new ArrayList<>();
                if (bookList == null) {
                    books.add(new Book(01, "Java基础教程", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE));
                } else {
                    books = bookList;
                }
                // 写入硬盘文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
                oos.writeObject(books);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/

    /**
     * 统一化的数据初始操作。
     *  其中List<?> list中的 ? 为泛型通配符。
     * @param path  文件的路径
     * @param list  初始化数据
     */
    public static void initData(String path, List<?> list) {
        // 为了方便流的关闭，将输出对象流放在外面
        ObjectOutputStream oos = null;
        try {
            // 创建文件夹和文件
            File directory = new File(path.split("/")[0] + "/");
            // 防止出错以及重复写，命名一个常量直接调用
            File file = new File(path);
            // 判断文件夹是否存在
            if (!directory.exists()) {
                directory.mkdir();
            }
            // 判断文件是否存在
            if (!file.exists()) {
                file.createNewFile();
                // 利用对象输出流将users拷贝到文件中
                oos = new ObjectOutputStream(new FileOutputStream(path));
                // 将集合中的数据传入oos流中
                oos.writeObject(list);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
