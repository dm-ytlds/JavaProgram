package com.demi.crm.workbench.service.impl;

import com.demi.crm.settings.dao.UserDao;
import com.demi.crm.settings.domain.User;
import com.demi.crm.utils.SqlSessionUtil;
import com.demi.crm.vo.PaginationVO;
import com.demi.crm.workbench.dao.ActivityDao;
import com.demi.crm.workbench.dao.ActivityRemarkDao;
import com.demi.crm.workbench.domain.Activity;
import com.demi.crm.workbench.domain.ActivityRemark;
import com.demi.crm.workbench.service.ActivityService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    // 实例化两个市场活动相关的数据接入层对象
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    // 实例化用户数据接入层的对象
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public Boolean save(Activity activity) {

        boolean flag = true;
        // 返回受到影响的条数
        int count = activityDao.save(activity);
        if (count != 1) {
            // 添加失败
            flag = false;
        }
        return flag;
    }

    /**
     * 通过条件查询和分页查询获取所需要的数据
     * @param map
     * @return
     */
    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        // 总条数
        int total = activityDao.getTotalByCondition(map);
        // 取得dataList
        List<Activity> dataList = activityDao.getActivityByCondition(map);
//        System.out.println(Arrays.toString(dataList.toArray()));
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        // 查出需要删除的备注数量
        int count1 = activityRemarkDao.getCountByIds(ids);
        // 删除备注，返回收到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByIds(ids);
        // 如果实际删除的条数不等于活动标记表中对应活动的总条数
        if (count1 != count2) {
            flag = false;
        }
        // 删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length)
            flag = false;
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        // 取userList
        List<User> userList = userDao.getUserList();
        // 根据id取市场活动a
        Activity activity = activityDao.getById(id);
        // 将userList和a打包到map集合中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userList", userList);
        map.put("activity", activity);
        // 返回map集合
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count != 1)
            flag = false;
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }

    @Override
    public boolean deleteRemark(String remarkId) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(remarkId);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1) {
            flag = false;
        }
        return flag;

    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = activityDao.getActivityListByName(aname);
        return aList;
    }
}
