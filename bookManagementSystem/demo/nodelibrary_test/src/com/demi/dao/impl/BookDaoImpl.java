package com.demi.dao.impl;

import com.demi.bean.Book;
import com.demi.bean.PathConstant;
import com.demi.dao.BookDao;
import com.demi.utils.BeanUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookDaoImpl implements BookDao {
    /**
     * ��������ѯͼ����Ϣ
      */
    @Override
    public List<Book> query(Book book) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH))) {
            // �õ���ѯ��ͼ����Ϣ
            List<Book> bookList = (List<Book>)ois.readObject();
            if (bookList != null) {
                // ��������bookΪ�գ�����book��isbnֵ������Ϊ�գ��򷵻�ȫ��ͼ����Ϣ
                if (book == null || ("".equals(book.getBookName()) && "".equals(book.getIsbn()))) {
                    return bookList;
                } else {
                    // �洢����������ͼ������
                    List<Book> conditionList =new ArrayList<>();

                    // �����������ҵ�Ҫ���ҵ�ͼ������
                    // ����ͼ���Ų�ѯ���������Ľ��ļ�¼��ӣ�
                    if (!(0 == book.getId())) {
                        conditionList = bookList.stream().filter(b -> b.getId() == book.getId()).collect(Collectors.toList());
                        return conditionList;
                    }
                    // 1.������������ͬʱ����Ĳ�ѯ����
                    if (!"".equals(book.getBookName()) && !"".equals(book.getIsbn())) {
                        conditionList = bookList.stream().filter(b -> b.getBookName().equals(book.getBookName())).collect(Collectors.toList());
                        conditionList = conditionList.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).collect(Collectors.toList());
                    } else {
                        // 2.�ڸ��ݵ�һ��������ѯ
                        // (1)����ͼ�����Ʋ�ѯ
                        if (!"".equals(book.getBookName())) {
                            conditionList = bookList.stream().filter(b -> b.getBookName().equals(book.getBookName())).collect(Collectors.toList());
                        }
                        // (2)����ͼ���isbn����ѯ
                        if (!"".equals(book.getIsbn())) {
                            conditionList = bookList.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).collect(Collectors.toList());
                        }
                    }
                    // ���ز�ѯ���
                    return conditionList;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * ���ͼ�����
     * @param book
     */
    @Override
    public void add(Book book) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // �ȴ����е�ͼ����Ϣ���ҵ����һ��ͼ��ı��
            ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH));
            List<Book> bookList = (List<Book>) ois.readObject();
            if (bookList != null) {
                int lastBID = bookList.get(bookList.size() - 1).getId();
                book.setId(lastBID + 1);
            } else {
                // ����һ���ռ���
                bookList = new ArrayList<>();
                // �������ʱû�����idֵ����������ļ���û�г�ʼidֵ������Ҫ�ֶ���ֵ
                book.setId(01);
            }
            // ����ͼ����Ϣ��ӵ�bookList������
            bookList.add(book);
            // �����ݳ־û����ļ���
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
            oos.writeObject(bookList);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������������ϵ����Ա����");
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ɾ��ͼ����Ϣ�Ĳ���
     * @param id
     */
    @Override
    public void delete(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // ��ѯ�����е�ͼ����Ϣ
            ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH));
            List<Book> bookList = (List<Book>) ois.readObject();
            if (bookList != null) {
                // ��stream��������Ҫɾ����ͼ��
                Book delBook = bookList.stream().filter(book -> book.getId() == id).findFirst().get();
                bookList.remove(delBook);
                // ��ɾ����ļ�������д�뵽�ļ���
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
                oos.writeObject(bookList);
                oos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("������������ϵ����Ա����");
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �޸�ͼ����Ϣ����
     * @param book
     */
    @Override
    public void update(Book book) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH));
            List<Book> bookList = (List<Book>) ois.readObject();
            if (bookList != null) {
                // �����ݼ������ҳ���Ҫ�޸ĵ�ͼ����Ϣ
                Book updateBook = bookList.stream().filter(book1 -> book1.getId() == book.getId()).findFirst().get();
                // �޸�ͼ����Ϣ
                // ͨ��BeanUtil����ʵ��
                BeanUtil.fieldTemplate(updateBook, book);
                // ���޸ĺ�����ݳ־û����ļ���
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
                oos.writeObject(bookList);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try{
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
