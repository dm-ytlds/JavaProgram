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
     * ������ѯ����
     * @param lend
     * @return
     */
    @Override
    public List<Lend> query(Lend lend) {
        ObjectInputStream ois = null;
        try {
            // ���ļ��в�ѯ���ļ�¼
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lendList = (List<Lend>) ois.readObject();
            if (lendList != null) {
                // ���û�в�ѯ��������ȫ��չʾ
                if (lend == null || ("".equals(lend.getBook().getBookName()) && "".equals(lend.getBook().getIsbn()))) {
                    return lendList;
                } else {
                    // �����洢���������Ľ��ļ�¼
                    List<Lend> conditionLends = new ArrayList<>();
                    // ���������������
                    if (!"".equals(lend.getBook().getBookName()) && !"".equals(lend.getBook().getIsbn())) {
                        conditionLends = lendList.stream().filter(l -> l.getBook().getBookName().equals(lend.getBook().getBookName())).collect(Collectors.toList());
                        // �����еĻ������ٴβ�ѯ
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

    // ��ӽ�����Ϣ����
    @Override
    public void add(Lend lend) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // �Ȳ���Ѿ����ڵ�
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lends = (List<Lend>) ois.readObject();
            // ��������Ҫ��ӵļӽ�ȥ
            lends.add(lend);
            // �ڽ���������д���ļ���
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
     * ɾ������
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

            // ����д���ļ���
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
     * ���²���
     * @param lend
     */
    @Override
    public void update(Lend lend) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // ���ļ��в�����ļ�¼
            ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH));
            List<Lend> lendList = (List<Lend>) ois.readObject();
            if (lendList != null) {
                Lend originLend = lendList.stream().filter(l -> l.getId().equals(lend.getId())).findFirst().get();
                // ���½�Լ����
                BeanUtil.fieldTemplate(originLend, lend);
                // �־û����ļ���
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
