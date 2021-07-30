package com.demi.service.impl;

import com.demi.bean.Lend;
import com.demi.dao.LendDao;
import com.demi.dao.impl.LendDaoImpl;
import com.demi.service.LendService;

import java.util.List;

public class LendServiceImpl implements LendService {

    private LendDao lendDao = new LendDaoImpl();

    @Override
    public List<Lend> select(Lend lend) {
        return lendDao.select(lend);
    }
}
