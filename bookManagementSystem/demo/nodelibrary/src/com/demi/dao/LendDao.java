package com.demi.dao;

import com.demi.bean.Lend;

import java.util.List;

public interface LendDao {
    List<Lend> select(Lend lend);
}
