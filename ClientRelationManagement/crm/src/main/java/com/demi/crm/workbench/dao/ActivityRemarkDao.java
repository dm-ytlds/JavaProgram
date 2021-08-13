package com.demi.crm.workbench.dao;

public interface ActivityRemarkDao {
    // 根据id值，获取需要删除的备注数量
    int getCountByIds(String[] ids);
    // 根据id值，删除备注
    int deleteByIds(String[] ids);
}
