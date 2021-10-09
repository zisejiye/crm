package com.bipowernode.crm.workbench.web.controller;

import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.settings.service.UserService;
import com.bipowernode.crm.settings.service.impl.UserServiceImpl;
import com.bipowernode.crm.utils.*;
import com.bipowernode.crm.vo.PageinationVo;
import com.bipowernode.crm.workbench.domain.Activity;
import com.bipowernode.crm.workbench.domain.ActivityRemark;
import com.bipowernode.crm.workbench.service.ActivityServic;
import com.bipowernode.crm.workbench.service.impl.ActivityServicImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            System.out.println("进入市场活动控制器");
        String path =  request.getServletPath();
        if("/workbench/Activity/getUserList.do".equals(path)){
           getUserList(request,response);
        }else if("/workbench/Activity/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/Activity/pageList.do".equals(path)) {
            pageList(request, response);
        }else if("/workbench/Activity/delete.do".equals(path)) {
            delete(request, response);
        }else if("/workbench/Activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        }else if("/workbench/Activity/update.do".equals(path)) {
            update(request, response);
        }else if("/workbench/Activity/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/Activity/getRemarkListAid.do".equals(path)) {
            getRemarkListAid(request, response);
        }else if("/workbench/Activity/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        }else if("/workbench/Activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }else if("/workbench/Activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改备注的操作");
        String id = request.getParameter("id");
        String noteContent =request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar  = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setId(id);
        ar.setEditTime(editTime);
        ActivityServic  as = (ActivityServic) ServiceFactory.getService(new ActivityServicImpl());
        boolean flag = as.updateRemark(ar);
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);


    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加增加备注选项");
        String noteConternt = request.getParameter("noteContent");
        String activityId   = request.getParameter("activityId");
        String  id = UUIDUtil.getUUID();
        //创建时间，当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，当前系统用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String  editFlag = "0";

        ActivityRemark  ar = new ActivityRemark();
        ar.setActivityId(activityId );
        ar.setId(id);
        ar.setCreateBy(createBy );
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);
        ar.setNoteContent(noteConternt);
        ActivityServic  as = (ActivityServic) ServiceFactory.getService(new ActivityServicImpl());
        boolean flag =as.saveRemark(ar);
        Map<String,Object>  map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);




    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入备注删除选项");
        String  id = request.getParameter("id");
        ActivityServic  as = (ActivityServic) ServiceFactory.getService(new ActivityServicImpl());
        boolean  flag =as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getRemarkListAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id， 取得市场备注信息");
        String activityId = request.getParameter("activityId");
        ActivityServic  as = (ActivityServic) ServiceFactory.getService(new ActivityServicImpl());
        List<ActivityRemark> arList =as.getRemarkListAid( activityId );
        PrintJson.printJsonObj(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详细信息页操作");
        String id = request.getParameter("id");
        ActivityServic  as = (ActivityServic) ServiceFactory.getService(new ActivityServicImpl());
        Activity a= as.detail(id);
        request.setAttribute("a",a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改用户信息操作");
        String   id       = request.getParameter("id");
        String   owner    = request.getParameter("owner");
        String   name     =request.getParameter("name");
        String   startDate   =request.getParameter("startDate");
        String   endDate   =request.getParameter("endDate");
        String    cost     =request.getParameter("cost");
        String   description    =request.getParameter("description");
        //修改时间，当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人，当前系统用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a= new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityServic as =(ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        boolean flag =as.update(a);
        System.out.println(flag);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入获得用户信息和市场活动信息列表");
        String  id=  request.getParameter("id");
        ActivityServic  as = (ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        Map<String,Object> map =as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到市场活动信息删除操作，");

        String ids[] = request.getParameterValues("id");
        ActivityServic  as = (ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        boolean  flage =as.delete(ids);
        PrintJson.printJsonFlag(response,flage);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询市场活动信息列表的操作（结合条件查询+分页查询）");
        //获得前端页面信参数
        String   name =request.getParameter("name");
        String  owner =request.getParameter("owner");
        String   startDate =request.getParameter("startDate");
        String   endDate =request.getParameter("endDate");
        String   pageNoStr =request.getParameter("pageNo");
        int  pageNo = Integer.valueOf(pageNoStr);
        String   pageSizeStr =request.getParameter("pageSize");
        //每页展示的记录数
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name );
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate", endDate);
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("skipCount", skipCount);
        ActivityServic  as = (ActivityServic)ServiceFactory.getService(new ActivityServicImpl());

          PageinationVo<Activity> vo =as.pageList(map);
          PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动添加操作");

      String   id       = UUIDUtil.getUUID();
      String   owner    = request.getParameter("owner");
      String   name     =request.getParameter("name");
      String   startDate   =request.getParameter("startDate");
      String   endDate   =request.getParameter("endDate");
      String    cost     =request.getParameter("cost");
      String   description    =request.getParameter("description");
      //创建时间，当前系统时间
      String createTime = DateTimeUtil.getSysTime();
      //创建人，当前系统用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

         Activity a= new Activity();
         a.setId(id);
         a.setOwner(owner);
         a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityServic as =(ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
           boolean flag =as.save(a);
        System.out.println(flag);
            PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService userservice=(UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList= userservice.getUserList();
        //遍历出用户的姓名
        for (User ni:uList) {
            System.out.println(ni.getName());

        }

        PrintJson.printJsonObj(response,uList);
    }
}
