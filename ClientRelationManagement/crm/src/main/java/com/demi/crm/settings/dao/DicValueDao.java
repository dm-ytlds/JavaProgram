package com.demi.crm.settings.dao;

import com.demi.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
