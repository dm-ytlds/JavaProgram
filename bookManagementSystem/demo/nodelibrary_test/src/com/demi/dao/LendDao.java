package com.demi.dao;


import com.demi.bean.Lend;

import java.util.List;

/**
 * 借阅管理模块，数据持久化操作接口
 */
public interface LendDao {
    // 条件查询
    List<Lend> query(Lend lend);

    // 添加操作
    void add(Lend lend);

    // 删除操作
    void delete(String id);

    // 更新操作
    void update(Lend lend);

}
