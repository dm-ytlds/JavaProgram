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
    �û����ݹ���
 */
public class InitDataUtil {
    public static void main(String[] args) {
        // test
        // ��ʼ���û�����
        List<User> userList = new ArrayList<>();
        userList.add(new User(2021001, "demi", Constant.USER_OK, new BigDecimal(100), false));
        userList.add(new User(2021002, "lee", Constant.USER_FROZEN, BigDecimal.TEN, false));
        userList.add(new User(2021003, "john", Constant.USER_OK, BigDecimal.ZERO, false));
        initData(PathConstant.USER_PATH, userList);

        // ��ʼ��ͼ������
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(01, "Java�����̳�", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE));
        bookList.add(new Book(02, "Java���׽̳�", "ddemi", Constant.TYPE_COMPUTER, "001-002", "qingHua University", Constant.STATUS_STORAGE));
        initData(PathConstant.BOOK_PATH, bookList);

        // ��ʼ��ͼ���������
        List<Lend> lendList = new ArrayList<>();
        // ������
        User user = new User(1001, "demi", Constant.USER_OK, new BigDecimal(100), false);
        // �����鼮
        Book book = new Book(01, "Java�����̳�", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE);
        Lend lend = new Lend();
        // ʹ��UUID���ɽ��ı��
        lend.setId(UUID.randomUUID().toString());
        lend.setBook(book);
        lend.setUser(user);
        lend.setStatus(Constant.LEND_LEND);
        // ����ͼ������ļ��е�ͼ��״̬
        BookDao bookDao = new BookDaoImpl();
        book.setStatus(Constant.STATUS_LEND);
        bookDao.update(book);
        // ��������
        LocalDate begin = LocalDate.now();
        lend.setLendDate(begin);
        // �黹����
        lend.setReturnDate(begin.plusDays(30));
        // ��������Ϣ���뼯����
        lendList.add(lend);
        initData(PathConstant.LEND_PATH, lendList);
    }

   /* *//**
     * ��ʼ���û�����
     * @param userList
     *//*
    public static void initUser(List<User> userList) {
        // Ϊ�˷������Ĺرգ��������������������
        ObjectOutputStream oos = null;
        try {
            // �����ļ��к��ļ�
            File directory = new File("user/");
            // ��ֹ�����Լ��ظ�д������һ������ֱ�ӵ���
            File file = new File(PathConstant.USER_PATH);
            // �ж��ļ����Ƿ����
            if (!directory.exists()) {
                directory.mkdir();
            }
            // �ж��ļ��Ƿ����
            if (!file.exists()) {
                file.createNewFile();
                List<User> users = new ArrayList<>();
                // ����ļ���û�����ݣ����Գ�ʼ��һЩ����
                if (userList == null) {
                    // ��������״ֵ̬
                    users.add(new User(1001, "demi", Constant.USER_OK, new BigDecimal(100)));
                    users.add(new User(1002, "lee", Constant.USER_FROZEN, BigDecimal.TEN));
                } else {
                    users = userList;
                }
                // ���ö����������users�������ļ���
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                // �������е����ݴ���oos����
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
     * ��ʼ��ͼ����Ϣ
     * @param bookList
     *//*
    private static void initBook(List<Book> bookList) {

        ObjectOutputStream oos = null;
        // ��ʼ���ļ�Ŀ¼�����ļ�
        File directory = new File("book/");
        File file = new File(PathConstant.BOOK_PATH);
        // �ж��ļ����Ƿ����
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                // �ļ������ڣ��մ������ļ�Ҳ��Ȼû������
                // ��ʼ������ͼ����Ϣ
                List<Book> books = new ArrayList<>();
                if (bookList == null) {
                    books.add(new Book(01, "Java�����̳�", "demi", Constant.TYPE_COMPUTER, "001-001", "xiHua University", Constant.STATUS_STORAGE));
                } else {
                    books = bookList;
                }
                // д��Ӳ���ļ���
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
     * ͳһ�������ݳ�ʼ������
     *  ����List<?> list�е� ? Ϊ����ͨ�����
     * @param path  �ļ���·��
     * @param list  ��ʼ������
     */
    public static void initData(String path, List<?> list) {
        // Ϊ�˷������Ĺرգ��������������������
        ObjectOutputStream oos = null;
        try {
            // �����ļ��к��ļ�
            File directory = new File(path.split("/")[0] + "/");
            // ��ֹ�����Լ��ظ�д������һ������ֱ�ӵ���
            File file = new File(path);
            // �ж��ļ����Ƿ����
            if (!directory.exists()) {
                directory.mkdir();
            }
            // �ж��ļ��Ƿ����
            if (!file.exists()) {
                file.createNewFile();
                // ���ö����������users�������ļ���
                oos = new ObjectOutputStream(new FileOutputStream(path));
                // �������е����ݴ���oos����
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
