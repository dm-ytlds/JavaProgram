package com.demi.crm.workbench.dao;

import com.demi.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int unBund(String id);

    int bund(ClueActivityRelation car);
}
