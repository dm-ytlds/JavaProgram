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
     * 按条件查询图书信息
      */
    @Override
    public List<Book> query(Book book) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH))) {
            // 得到查询的图书信息
            List<Book> bookList = (List<Book>)ois.readObject();
            if (bookList != null) {
                // 如果传入的book为空，或者book的isbn值和书名为空，则返回全部图书信息
                if (book == null || ("".equals(book.getBookName()) && "".equals(book.getIsbn()))) {
                    return bookList;
                } else {
                    // 存储符合条件的图书数据
                    List<Book> conditionList =new ArrayList<>();

                    // 根据条件，找到要查找的图书数据
                    // 根据图书编号查询（方便后面的借阅记录添加）
                    if (!(0 == book.getId())) {
                        conditionList = bookList.stream().filter(b -> b.getId() == book.getId()).collect(Collectors.toList());
                        return conditionList;
                    }
                    // 1.先做两个条件同时输入的查询操作
                    if (!"".equals(book.getBookName()) && !"".equals(book.getIsbn())) {
                        conditionList = bookList.stream().filter(b -> b.getBookName().equals(book.getBookName())).collect(Collectors.toList());
                        conditionList = conditionList.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).collect(Collectors.toList());
                    } else {
                        // 2.在根据单一条件做查询
                        // (1)根据图书名称查询
                        if (!"".equals(book.getBookName())) {
                            conditionList = bookList.stream().filter(b -> b.getBookName().equals(book.getBookName())).collect(Collectors.toList());
                        }
                        // (2)根据图书的isbn来查询
                        if (!"".equals(book.getIsbn())) {
                            conditionList = bookList.stream().filter(b -> b.getIsbn().equals(book.getIsbn())).collect(Collectors.toList());
                        }
                    }
                    // 返回查询结果
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
     * 添加图书操作
     * @param book
     */
    @Override
    public void add(Book book) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 先从已有的图书信息中找到最后一个图书的编号
            ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH));
            List<Book> bookList = (List<Book>) ois.readObject();
            if (bookList != null) {
                int lastBID = bookList.get(bookList.size() - 1).getId();
                book.setId(lastBID + 1);
            } else {
                // 创建一个空集合
                bookList = new ArrayList<>();
                // 由于添加时没有添加id值，所以如果文件中没有初始id值，就需要手动赋值
                book.setId(01);
            }
            // 将该图书信息添加到bookList集合中
            bookList.add(book);
            // 将数据持久化到文件中
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
            oos.writeObject(bookList);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("操作错误，请联系管理员！！");
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
     * 删除图书信息的操作
     * @param id
     */
    @Override
    public void delete(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 查询出已有的图书信息
            ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH));
            List<Book> bookList = (List<Book>) ois.readObject();
            if (bookList != null) {
                // 用stream流查找想要删除的图书
                Book delBook = bookList.stream().filter(book -> book.getId() == id).findFirst().get();
                bookList.remove(delBook);
                // 将删除后的集合重新写入到文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.BOOK_PATH));
                oos.writeObject(bookList);
                oos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("操作错误，请联系管理员！！");
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
     * 修改图书信息操作
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
                // 从数据集合中找出想要修改的图书信息
                Book updateBook = bookList.stream().filter(book1 -> book1.getId() == book.getId()).findFirst().get();
                // 修改图书信息
                // 通过BeanUtil类来实现
                BeanUtil.fieldTemplate(updateBook, book);
                // 将修改后的数据持久化到文件中
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
