package com.demi.crm.workbench.dao;

import com.demi.crm.workbench.domain.Clue;

public interface ClueDao {
    int save(Clue clue);

    Clue detail(String id);
}
