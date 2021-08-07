package com.demi.crm.workbench.service.impl;

import com.demi.crm.utils.SqlSessionUtil;
import com.demi.crm.workbench.dao.ActivityDao;
import com.demi.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
}
