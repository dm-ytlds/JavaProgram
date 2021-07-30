package com.demi.dao.impl;

import com.demi.bean.Lend;
import com.demi.bean.PathConstant;
import com.demi.dao.LendDao;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class LendDaoImpl implements LendDao {

    /**
     *  ��ѯ
     * @param lend
     * @return
     */
    @Override
    public List<Lend> select(Lend lend) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.LEND_PATH))) {

            List<Lend> list = (List<Lend>)ois.readObject();
            if (lend == null || "".equals(lend.getId())) {
                return list;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return new ArrayList<>();
    }
}
