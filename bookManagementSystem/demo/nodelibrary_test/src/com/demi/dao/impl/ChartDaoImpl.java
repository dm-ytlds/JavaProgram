package com.demi.dao.impl;

import com.demi.bean.Book;
import com.demi.bean.PathConstant;
import com.demi.dao.ChartDao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartDaoImpl implements ChartDao {

    /**
     * ͳ��ͼ����������
     * @return
     */
    @Override
    public Map<String, Integer> bookTypeCount() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.BOOK_PATH))) {
            // �õ��ļ��е�ͼ������
            List<Book> bookList = (List<Book>) ois.readObject();
            // stream����ͼ����з���ͳ��
            Map<String, List<Book>> collect = bookList.stream().collect(Collectors.groupingBy(Book::getType));
            // ��������õ��Ľ��
            Map<String, Integer> map = new HashMap<>();
            Iterator<Map.Entry<String, List<Book>>> iterator = collect.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<Book>> next = iterator.next();
                // ��collect���ϵ�key�Լ�ֵ����������map������
                map.put(next.getKey(), next.getValue() == null ? 0 : next.getValue().size());
            }
            return map;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
