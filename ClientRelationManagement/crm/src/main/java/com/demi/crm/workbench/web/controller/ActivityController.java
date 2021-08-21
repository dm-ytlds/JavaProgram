package com.demi.crm.workbench.web.controller;

import com.demi.crm.settings.domain.User;
import com.demi.crm.settings.service.UserService;
import com.demi.crm.settings.service.impl.UserServiceImpl;
import com.demi.crm.utils.DateTimeUtil;
import com.demi.crm.utils.PrintJson;
import com.demi.crm.utils.ServiceFactory;

import com.demi.crm.utils.UUIDUtil;
import com.demi.crm.vo.PaginationVO;
import com.demi.crm.workbench.domain.Activity;
import com.demi.crm.workbench.domain.ActivityRemark;
import com.demi.crm.workbench.service.ActivityService;
import com.demi.crm.workbench.service.impl.ActivityServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resq) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");
        // Servlet路径
        String path = req.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(req, resq);

        } else if ("/workbench/activity/save.do".equals(path)) {
            // 保存创建的市场活动
            save(req, resq);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            // 展示市场活动列表
            pageList(req, resq);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(req, resq);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(req, resq);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(req, resq);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(req, resq);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(req, resq);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(req, resq);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(req, resq);
        } else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(req, resq);
        }/*else if ("/workbench/activity/updateActivityByDetailModal.do".equals(path)) {
            updateActivityByDetailModal(req, resq);
        }*/
    }


    private void updateRemark(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行修改备注内容的操作");
        String remarkId = req.getParameter("remarkId");
        String noteContent = req.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(remarkId);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = aService.updateRemark(ar);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(resq, map);
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行添加市场活动备注信息的操作");
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) req.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        // 封装到ActivityRemark对象中
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = aService.saveRemark(ar);
        // 将信息返回给前端，用来展示添加的备注信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(resq, map);

    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行删除市场活动备注信息的操作");
        String remarkId =  req.getParameter("remarkId");
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = aService.deleteRemark(remarkId);
        PrintJson.printJsonFlag(resq, flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("根据市场活动id，取得备注信息列表");
        String activityId = req.getParameter("activityId");
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList = aService.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(resq, arList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resq) throws ServletException, IOException{
        System.out.println("展示市场活动的详细信息");
        String id = req.getParameter("id");
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = aService.detail(id);

        req.setAttribute("activity", activity);
        // 转发重定向操作
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req, resq);
    }

    private void update(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行更新市场活动信息的操作");

        String id = req.getParameter("id");
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        // 修改时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改时间
        String editBy = ((User)req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = activityService.update(activity);
        PrintJson.printJsonFlag(resq, flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行获取用户列表以及通过市场活动id查询单条市场活动的操作");
        String id = req.getParameter("id");
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /*
        * controller调用service的方法，返回值应该是什么。
        *   得从前端需要什么来切入，将前端需要的向service层索取。
        * 由于这里的两项信息复用率不高，直接用map集合实现就可以了。
        * */

        Map<String, Object> map = aService.getUserListAndActivity(id);
        PrintJson.printJsonObj(resq, map);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行删除市场活动记录操作");
        // 同一个key 多个value 使用数组
        String[] ids = req.getParameterValues("id");
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = aService.delete(ids);
        PrintJson.printJsonFlag(resq, flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("查询市场活动信息列表的操作，结合条件查询和分页查询");
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        int pageNo = Integer.parseInt(pageNoStr);
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize = Integer.parseInt(pageSizeStr);

        // 计算出略过的记录数
        int skipCount = (pageNo - 1) * pageSize;
        // 用Map集合实现信息封装
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /**
         * 分析：
         * 需要从后端取的数据有：
         *  (1) 市场活动信息列表；
         *  (2) 活动的总条数
         * 作为controller，需要为ajax请求提供多项信息
         *                 可以有两种方式来处理：
         *                     (1) 将多项信息打包成map，将map解析为json串
         *                     (2) 创建一个vo
         *
         *                 如何选择用哪种方式：
         *                     如果需要展现的信息将来还会大量用到，就需要创建一个vo类，这样使用更方便；
         *                     如果对于展现的信息只有在这个需求中能够使用，就用map集合
         *                 这里选用vo形式来存数据
         */
        // 调用创建的vo类，实例化后端返回的数据
        PaginationVO<Activity> vo = aService.pageList(map);
        // vo中的值转换成json形式
        PrintJson.printJsonObj(resq, vo);

    }

    /**
     * 执行市场活动的添加操作
     * @param req
     * @param resq
     */
    private void save(HttpServletRequest req, HttpServletResponse resq) {
        System.out.println("执行市场活动的添加操作");

        // 向数据库中存放数据
        // 用写好的工具生成
        String id = UUIDUtil.getUUID();
        // 从前端填写的json文件传递的data中获取
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");
        // 创建时间为当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人为当前登录的用户：通过当前会话，获取当前的user参数，并取出用户姓名
        String createBy = ((User)req.getSession().getAttribute("user")).getName();

        // 将获得的参数封装成Activity对象，方便传递到后台
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        // 实例化市场活动对象
        ActivityService aService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        // 将活动传入市场活动服务层
        Boolean flag = aService.save(activity);
        PrintJson.printJsonFlag(resq, flag);
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
