package com.demi.crm.workbench.service.impl;

import com.demi.crm.utils.SqlSessionUtil;
import com.demi.crm.utils.UUIDUtil;
import com.demi.crm.workbench.dao.ClueActivityRelationDao;
import com.demi.crm.workbench.dao.ClueDao;
import com.demi.crm.workbench.domain.Clue;
import com.demi.crm.workbench.domain.ClueActivityRelation;
import com.demi.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao card = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1)
            flag = false;
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unBund(String id) {
        boolean flag = true;
        int count = card.unBund(id);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag = true;
        for (String aid : aids) {
            // 将cid和每一个aid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            int count = card.bund(car);
            if (count != 1)
                flag = false;
        }
        return flag;
    }
}
