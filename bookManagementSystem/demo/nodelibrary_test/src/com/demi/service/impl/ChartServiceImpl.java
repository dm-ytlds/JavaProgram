package com.demi.service.impl;

import com.demi.dao.ChartDao;
import com.demi.dao.impl.ChartDaoImpl;
import com.demi.service.ChartService;

import java.util.Map;

public class ChartServiceImpl implements ChartService {
    ChartDao chartDao = new ChartDaoImpl();
    @Override
    public Map<String, Integer> bookTypeCount() {
        return chartDao.bookTypeCount();
    }
}
