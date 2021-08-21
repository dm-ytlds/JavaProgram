package com.demi.crm.workbench.web.controller;

import com.demi.crm.settings.domain.User;
import com.demi.crm.settings.service.UserService;
import com.demi.crm.settings.service.impl.UserServiceImpl;
import com.demi.crm.utils.*;
import com.demi.crm.vo.PaginationVO;
import com.demi.crm.workbench.domain.Activity;
import com.demi.crm.workbench.domain.ActivityRemark;
import com.demi.crm.workbench.domain.Clue;
import com.demi.crm.workbench.service.ActivityService;
import com.demi.crm.workbench.service.ClueService;
import com.demi.crm.workbench.service.impl.ActivityServiceImpl;
import com.demi.crm.workbench.service.impl.ClueServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resq) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");
        // Servlet路径
        String path = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(req, resq);

        } else if ("/workbench/clue/save.do".equals(path)) {
            // 保存创建的市场活动
            save(req, resq);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(req, resq);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(req, resq);
        } else if ("/workbench/clue/unBund.do".equals(path)) {
            unBund(req, resq);
        } else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {
            getActivityListByNameAndNotByClueId(req, resq);
        } else if ("/workbench/clue/bund.do".equals(path)) {
            bund(req, resq);
        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(req, resq);
        }else if ("/workbench/clue/convert.do".equals(path)) {
            convert(req, resq);
        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行转换页面的线索转换操作");
        String activityId = req.getParameter("clueId");
        String flag = req.getParameter("flag");
        // 首先的判断是表单传的数据（有创建交易的情况），还是挂参数传的数据（没有创建交易的情况）
        if ("a".equals(flag)) {
            // 有创建交易的情况
            
        } else {
            // 没有创建交易的情况

        }
    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("根据市场活动名称模糊查询");
        String aname = req.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(aname);
        PrintJson.printJsonObj(resq, aList);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("为线索关联市场活动的操作");
        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.bund(cid, aids);
        PrintJson.printJsonFlag(resq, flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("查询市场活动列表，根据市场活动名称模糊查询，排除已经关联指定线索的列表");
        String aname = req.getParameter("aname");
        String clueId = req.getParameter("clueId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("aname", aname);
        map.put("clueId", clueId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(resq, aList);
    }

    private void unBund(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("解除关联操作");
        String id = req.getParameter("id");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.unBund(id);
        PrintJson.printJsonFlag(resq, flag);
    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("查询市场活动信息列表(根据线索id)");
        String clueId = req.getParameter("clueId");
        System.out.println(clueId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(resq, aList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resq) throws ServletException, IOException {
        System.out.println("执行线索详情页");
        String id = req.getParameter("id");
        System.out.println(id);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.detail(id);
        req.setAttribute("clue", clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req, resq);
    }

    private void save(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行线索的添加操作");
        // 从前端得到需要传给数据库的信息
        String id = UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String createBy = ((User)req.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        // 封装
        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(resq, flag);

    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("获取用户信息列表(clue)");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(resq, userList);
    }
}
