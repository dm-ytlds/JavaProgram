package com.demi.crm.settings.service.impl;

import com.demi.crm.settings.dao.DicTypeDao;
import com.demi.crm.settings.dao.DicValueDao;
import com.demi.crm.settings.domain.DicType;
import com.demi.crm.settings.domain.DicValue;
import com.demi.crm.settings.service.DicService;
import com.demi.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    /*
    * 从数据库中获取上下文域对象所需要的数据
    * */
    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        // 将字典类型列表取出
        List<DicType> types = dicTypeDao.getTypeList();
        // 将字典类型列表遍历，取出每一种类型的字典类型编码
        for (DicType type : types) {
            String code = type.getCode();
            // 根据字典类型取字典值
            List<DicValue> values = dicValueDao.getValueListByCode(code);
            map.put(code, values);
        }
        return map;
    }
}
