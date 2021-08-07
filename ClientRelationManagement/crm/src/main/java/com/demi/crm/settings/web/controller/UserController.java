package com.demi.crm.settings.web.controller;

import com.demi.crm.settings.domain.User;
import com.demi.crm.settings.service.UserService;
import com.demi.crm.settings.service.impl.UserServiceImpl;
import com.demi.crm.utils.MD5Util;
import com.demi.crm.utils.PrintJson;
import com.demi.crm.utils.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入用户控制器");

        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            login(req, resp);
        } else if ("/settings/user/save.do".equals(path)) {

        }

    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到验证登录操作");
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");
        // 将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接收ip地址
        String ip = req.getRemoteAddr();

        // 创建用户服务对象
        // 统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = us.login(loginAct, loginPwd, ip);
            req.getSession().setAttribute("user", user);
            // 如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            // 表示登录成功
            // 用打包好的工具类实现信息返回
            PrintJson.printJsonFlag(resp, true);

        } catch (Exception e) {
            e.printStackTrace();
            // 如果程序执行了catch 语句块，说明业务层的验证失败了，为controller跑出了异常信息
            // 需要返回错误信息
            String msg = e.getMessage();
            /*
                作为controller，需要为ajax请求提供多项信息
                可以有两种方式来处理：
                    (1) 将多项信息打包成map，将map解析为json串
                    (2) 创建一个vo
                        private boolean success;
                        private String msg;
                如何选择用哪种方式：
                    如果需要展现的信息将来还会大量用到，就需要创建一个vo类，这样使用更方便；
                    如果对于展现的信息只有在这个需求中能够使用，就用map集合
             */
            Map<String ,Object> map = new HashMap<String, Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(resp, map);

        }
    }

}
