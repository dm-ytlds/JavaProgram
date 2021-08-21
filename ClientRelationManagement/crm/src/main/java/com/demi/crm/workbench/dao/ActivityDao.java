package com.demi.crm.workbench.dao;

import com.demi.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    // 条件获取市场活动信息列表的总条数
    int getTotalByCondition(Map<String, Object> map);

    // 条件获取市场活动信息列表
    List<Activity> getActivityByCondition(Map<String, Object> map);
    // 删除活动记录
    int delete(String[] ids);
    // 通过id获取市场活动信息
    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
