package com.demi.crm.workbench.web.controller;

import com.demi.crm.settings.domain.User;
import com.demi.crm.settings.service.UserService;
import com.demi.crm.settings.service.impl.UserServiceImpl;
import com.demi.crm.utils.PrintJson;
import com.demi.crm.utils.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resq) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");
        // Servlet路径
        String path = req.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(req, resq);

        } else if ("/workbench/activity/xxx.do".equals(path)) {

        }

    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("取得用户信息列表");
        // 与用户相关的操作
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        // 将对象数据转换成json形式
        PrintJson.printJsonObj(resq, userList);
    }
}
