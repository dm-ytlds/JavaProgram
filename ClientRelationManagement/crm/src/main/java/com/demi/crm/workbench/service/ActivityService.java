package com.demi.crm.workbench.service;

import com.demi.crm.vo.PaginationVO;
import com.demi.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);
}
