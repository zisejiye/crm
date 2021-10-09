package com.bipowernode.crm.web.filter;

import com.bipowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登录过得过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();
        //不应拦截，自动放行
        if ("/login.jsp".equals(path) ||"/settings/user/login.do".equals(path)) {
            chain.doFilter(req,resp);
            //验证是否登陆过，当前session中是否有user对象
        }else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //user不为null 说明登陆过，放行
            if (user != null) {
                chain.doFilter(req,resp);

            }else{
                //uers为null，未登陆过，重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login.jsp");

            }
        }
    }
}
