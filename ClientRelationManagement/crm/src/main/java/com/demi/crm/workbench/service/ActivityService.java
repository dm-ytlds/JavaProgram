package com.demi.crm.workbench.service;

import com.demi.crm.vo.PaginationVO;
import com.demi.crm.workbench.domain.Activity;
import com.demi.crm.workbench.domain.ActivityRemark;
import com.demi.crm.workbench.service.impl.ActivityServiceImpl;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String remarkId);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
