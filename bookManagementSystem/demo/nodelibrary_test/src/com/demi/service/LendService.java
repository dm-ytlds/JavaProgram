package com.demi.service;

import com.demi.bean.Lend;

import java.util.List;

/**
 * 图书借阅服务
 */
public interface LendService {
    List<Lend> query(Lend lend);

    // 添加借阅记录
    void add(int userId, int bookId);

    // 还书操作
    List<Lend> returnBook(Lend lend);

    // 更新操作
    void update(Lend lend);
}
