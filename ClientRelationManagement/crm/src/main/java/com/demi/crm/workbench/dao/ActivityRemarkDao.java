package com.demi.crm.workbench.dao;

import com.demi.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    // 根据id值，获取需要删除的备注数量
    int getCountByIds(String[] ids);
    // 根据id值，删除备注
    int deleteByIds(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String remarkId);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
