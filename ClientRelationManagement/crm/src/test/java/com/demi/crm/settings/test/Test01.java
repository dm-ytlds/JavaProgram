package com.demi.crm.settings.test;

import com.demi.crm.utils.DateTimeUtil;
import com.demi.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test01 {
    public static void main(String[] args) {
        /*// 验证失效时间
        String expireTime = "2021-08-04 15:24:50";

        // 当前系统时间
        String sysTime = DateTimeUtil.getSysTime();
        // 根据i值与0的大小比较得出是否时效
        int i = expireTime.compareTo(sysTime);*/

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        System.out.println(format);*/

        /*// 验证账号锁定状态
        String lockState = "0";
        if ("0".equals(lockState)) {
            System.out.println("账号已锁定");
        }*/

        /*// 验证Ip地址
        String allowIps = "192.168.1.1,192.168.1.2";

        String ip = "192.168.1.1";
        // 验证是否存在该Ip
        if (allowIps.contains(ip)) {
            System.out.println("地址有效，可登录");
        } else {
            System.out.println("地址无效，别登了");
        }*/

        // 密钥工具的测试
        String s = "";
        String md5 = MD5Util.getMD5(s);
        System.out.println(md5);
    }
}
