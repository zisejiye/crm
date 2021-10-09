package com.bipowernode.crm.settings.web.controller;

import com.bipowernode.crm.exception.LoginException;
import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.settings.service.UserService;
import com.bipowernode.crm.settings.service.impl.UserServiceImpl;
import com.bipowernode.crm.utils.MD5Util;
import com.bipowernode.crm.utils.PrintJson;
import com.bipowernode.crm.utils.ServiceFactory;
import com.bipowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");
        String path =  request.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/setting/user/xxx.do".equals(path)){
            /* xxx.(reqest,response);*/
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码进行加密为MD5形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接受浏览器ip
        String ip =request.getRemoteAddr();
        System.out.println("----------ip:"+ip);

        //业务层的开发，使用代理类的接口对象
        UserService us =(UserService) ServiceFactory.getService(new UserServiceImpl());

         try{

             User user =us.login(loginAct,loginPwd,ip);
             request.getSession().setAttribute("user",user);
             //程序到此处说明，业务层Controller没有抛出异常。向页面返回登陆成功信息

             PrintJson.printJsonFlag(response,true);

         }catch(Exception e){
             e.printStackTrace();

             //登录失败，响应登录失败信息及原因
             //{"success":true，"msg":?}
             String msg = e.getMessage();

             /*需要为ajax提供多项信息
                    1）将信息打包成map，将map解析成json串
                    2）创建一个Vo类
             * */
             Map<String,Object> map = new HashMap<String,Object>();
             map.put("success",false);
             map.put("msg",msg);

             PrintJson.printJsonObj(response,map);


        }
    }
}
