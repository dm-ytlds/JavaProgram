package com.demi.crm.web.filter;

import com.demi.crm.settings.domain.User;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 没登录，重定向到登录页
                /*重定向路径写法：
                    在实际开发中，对于路径的使用，不论操作的是前端还是后端，统一使用绝对路径
                关于转发和重定向路径的写法：
                转发：
                    使用的是一种特殊的绝对路径使用方式，即绝对路径前面不加"/"项目名，这种路径也称之为内部路径；
                    /login.jsp
                重定向：
                    使用的是传统绝对路径的写法。 /crm/login.jsp
                为什么使用重定向，而不是转发？
                    转发之后，路径停留在原来的路径上，不会跳转到最新资源的路径；
                    重定向就弥补了转发不能跳转路径的问题。*/
// 项目名称为："/crm/login.jsp"
// 项目名称动态获取request.getContextPath()

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("验证是否登录过的过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        String requestURI = request.getRequestURI();
//        System.out.println(requestURI);
        String servletPath = request.getServletPath();
//        System.out.println(servletPath);
        // 不用拦截
        //
        if ("/login.jsp".equals(servletPath) || "/settings/user/login.do".equals(servletPath)) {
//            System.out.println("afdsvsfbv");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // 说明用户登录过
                filterChain.doFilter(servletRequest, servletResponse);
            } else {

                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
/*public class LoginFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("验证是否登录过的过滤器");


        String servletPath = servletRequest.getServletPath();
        if ("/login.jsp".equals(servletPath) || "/setting/user/login.do".equals(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpSession session = servletRequest.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // 说明用户登录过
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // 没登录，重定向到登录页
                *//*重定向路径写法：
                    在实际开发中，对于路径的使用，不论操作的是前端还是后端，统一使用绝对路径
                关于转发和重定向路径的写法：
                转发：
                    使用的是一种特殊的绝对路径使用方式，即绝对路径前面不加"/"项目名，这种路径也称之为内部路径；
                    /login.jsp
                重定向：
                    使用的是传统绝对路径的写法。 /crm/login.jsp
                为什么使用重定向，而不是转发？
                    转发之后，路径停留在原来的路径上，不会跳转到最新资源的路径；
                    重定向就弥补了转发不能跳转路径的问题。*//*
                // 项目名称为："/crm/login.jsp"
                // 项目名称动态获取request.getContextPath()
                servletResponse.sendRedirect(servletRequest.getContextPath() + "/login.jsp");
            }
        }
    }
}*/
