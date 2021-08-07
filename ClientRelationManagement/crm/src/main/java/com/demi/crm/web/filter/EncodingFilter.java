package com.demi.crm.web.filter;

import jakarta.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("过滤字符编码");
        // 过滤post 请求中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");
        // 过滤响应流中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        // 将请求放行
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
