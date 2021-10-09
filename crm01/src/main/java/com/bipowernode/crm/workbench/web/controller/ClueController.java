package com.bipowernode.crm.workbench.web.controller;



import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.settings.service.UserService;
import com.bipowernode.crm.settings.service.impl.UserServiceImpl;
import com.bipowernode.crm.utils.*;
import com.bipowernode.crm.workbench.domain.Activity;
import com.bipowernode.crm.workbench.domain.Clue;
import com.bipowernode.crm.workbench.domain.Tran;
import com.bipowernode.crm.workbench.service.ActivityServic;
import com.bipowernode.crm.workbench.service.ClueService;
import com.bipowernode.crm.workbench.service.impl.ActivityServicImpl;
import com.bipowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        String path =  request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(path)){
         save(request,response);
        }else if("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        } else if("/workbench/clue/unbund.do".equals(path)) {
            unbund(request, response);
        }else if("/workbench/clue/getActivityListByNameAndNotClueId.do".equals(path)) {
            getActivityListByNameAndNotClueId(request, response);
        }else if("/workbench/clue/bund.do".equals(path)) {
            bund(request, response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        }else if("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("执行线索转换操作");
        String clueId = request.getParameter("clueId");
        //接受是否创建交易的标记
        String flag = request.getParameter("flag");
        Tran t=null;
        //如需创建交易
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        if("a".equals(flag)){
            t = new Tran();
            //接受交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1=cs.convert(clueId,t,createBy);
        if(flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查）");
        String aname = request.getParameter("aname");
        ActivityServic ac =(ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        List<Activity> aList =ac.getActivityListByName(aname);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动操作");
        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag =cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联指定线索的列表）");
        String aname = request.getParameter("aname");
        String clueId=request.getParameter("clueId");
        Map<String,String> map = new HashMap<String,String>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        ActivityServic ac =(ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        List<Activity> aList =ac.getActivityListByNameAndNotClueId(map);
        PrintJson.printJsonObj(response,aList);


    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行解除关联关系");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag= cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id查询市场活动信息列表");
        String clueId = request.getParameter("ClueId");
        ActivityServic ac =(ActivityServic)ServiceFactory.getService(new ActivityServicImpl());
        List<Activity>  aList =ac.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
        System.out.println("跳转到线索详细信息页");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c=cs.detail(id);

        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加用户信息列表的保存操作");
       String id = UUIDUtil.getUUID();
       String fullname = request.getParameter("fullname");
       String appellation = request.getParameter("appellation");
       String owner=request.getParameter("owner");
       String company=request.getParameter("company");
       String job=request.getParameter("job");
       String email=request.getParameter("email");
       String phone=request.getParameter("phone");
       String website=request.getParameter("website");
       String mphone=request.getParameter("mphone");
       String state=request.getParameter("state");
       String source=request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
       String description=request.getParameter("description");
       String contactSummary=request.getParameter("contactSummary");
       String nextContactTime=request.getParameter("nextContactTime");
       String address=request.getParameter("address");

        Clue c =new Clue();
        c.setId(id);
        c.setFullname( fullname);
        c.setAppellation( appellation);
        c.setOwner( owner);
        c.setCompany( company);
        c.setJob( job);
        c.setEmail( email);
        c.setPhone( phone);
        c.setWebsite( website);
        c.setMphone( mphone);
        c.setState( state);
        c.setSource( source);
        c.setCreateBy( createBy);
        c.setCreateTime( createTime);
        c.setDescription( description);
        c.setContactSummary( contactSummary);
        c.setNextContactTime( nextContactTime);
        c.setAddress( address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
         boolean flag=cs.save(c);

         PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService  us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User>   uList    = us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }


}
