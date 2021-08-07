package com.demi.crm.settings.test;

import com.demi.crm.settings.dao.UserDao;
import com.demi.crm.settings.domain.User;
import com.demi.crm.utils.MD5Util;
import com.demi.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class testdb {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Test
    public void test() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        Map<String, String> map = new HashMap<String, String>();
        String md5 = MD5Util.getMD5("123");
        map.put("loginAct", "ls");
        map.put("loginPwd", md5);
        User user = userDao.login(map);
        System.out.println(user);
    }
}
