package com.demi.dao.impl;


import com.demi.bean.Lend;
import com.demi.bean.PathConstant;
import com.demi.dao.LendDao;
import com.demi.utils.BeanUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LendDaoImpl implements LendDao {

    /**
     * 条件查询操作
     * @param lend
     * @return
     */
    @Override
    public List<Lend> query(Lend lend) {
        ObjectInputStream ois = null;
        try {
            // 从文件中查询借阅记录
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lendList = (List<Lend>) ois.readObject();
            if (lendList != null) {
                // 如果没有查询条件，则全部展示
                if (lend == null || ("".equals(lend.getBook().getBookName()) && "".equals(lend.getBook().getIsbn()))) {
                    return lendList;
                } else {
                    // 用来存储符合条件的借阅记录
                    List<Lend> conditionLends = new ArrayList<>();
                    // 如果两个条件都有
                    if (!"".equals(lend.getBook().getBookName()) && !"".equals(lend.getBook().getIsbn())) {
                        conditionLends = lendList.stream().filter(l -> l.getBook().getBookName().equals(lend.getBook().getBookName())).collect(Collectors.toList());
                        // 在已有的基础上再次查询
                        conditionLends = conditionLends.stream().filter(l -> l.getBook().getBookName().equals(lend.getBook().getBookName())).collect(Collectors.toList());
                    } else {
                        if (!"".equals(lend.getBook().getBookName())) {
                            conditionLends = lendList.stream().filter(l -> l.getBook().getBookName().equals(lend.getBook().getBookName())).collect(Collectors.toList());
                        }
                        if (!"".equals(lend.getBook().getIsbn())) {
                            conditionLends = lendList.stream().filter(l -> l.getBook().getBookName().equals(lend.getBook().getBookName())).collect(Collectors.toList());
                        }
                    }
                    return conditionLends;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    // 添加借阅信息操作
    @Override
    public void add(Lend lend) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 先查出已经存在的
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lends = (List<Lend>) ois.readObject();
            // 将现在需要添加的加进去
            lends.add(lend);
            // 在将整个数据写进文件中
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.LEND_PATH));
            oos.writeObject(lends);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
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
     * 删除操作
     * @param id
     */
    @Override
    public void delete(String id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lendList = (List<Lend>) ois.readObject();
            Lend lend = lendList.stream().filter(l -> l.getId().equals(id)).findFirst().get();
            lendList.remove(lend);

            // 重新写入文件中
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.LEND_PATH));
            oos.writeObject(lendList);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
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
     * 更新操作
     * @param lend
     */
    @Override
    public void update(Lend lend) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 从文件中查出借阅记录
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lendList = (List<Lend>) ois.readObject();
            if (lendList != null) {
                Lend originLend = lendList.stream().filter(l -> l.getId().equals(lend.getId())).findFirst().get();
                // 更新节约数据
                BeanUtil.fieldTemplate(originLend, lend);
                // 持久化到文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.LEND_PATH));
                oos.writeObject(lendList);
                oos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
