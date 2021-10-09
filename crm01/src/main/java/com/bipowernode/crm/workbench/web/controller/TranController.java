package com.bipowernode.crm.workbench.web.controller;


import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.settings.service.UserService;
import com.bipowernode.crm.settings.service.impl.UserServiceImpl;
import com.bipowernode.crm.utils.DateTimeUtil;
import com.bipowernode.crm.utils.PrintJson;
import com.bipowernode.crm.utils.ServiceFactory;
import com.bipowernode.crm.utils.UUIDUtil;
import com.bipowernode.crm.workbench.domain.Activity;
import com.bipowernode.crm.workbench.domain.Clue;
import com.bipowernode.crm.workbench.domain.Tran;
import com.bipowernode.crm.workbench.domain.TranHistory;
import com.bipowernode.crm.workbench.service.ActivityServic;
import com.bipowernode.crm.workbench.service.ClueService;
import com.bipowernode.crm.workbench.service.CustomerService;
import com.bipowernode.crm.workbench.service.TranService;
import com.bipowernode.crm.workbench.service.impl.ActivityServicImpl;
import com.bipowernode.crm.workbench.service.impl.ClueServiceImpl;
import com.bipowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bipowernode.crm.workbench.service.impl.TranServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.omg.CORBA.ObjectHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
           add(request,response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)) {
            save(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request,response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request,response);
        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计图表的数据");
        TranService ts =(TranService)ServiceFactory.getService(new TranServiceImpl());
        /*业务层返回
          total
          dataList
          通过map打包以上两项   进行返回
        * */
        Map<String, Object> map=ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行改变操作的阶段");
        String id= request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        TranService ts =(TranService)ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);
        Map<String,String> pMap =(Map<String,String>)this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));
        Map<String,Object>  map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",t);
        PrintJson.printJsonObj(response,map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取得响应的阶段历史列表");
        String tranId = request.getParameter("tranId");
        TranService ts =(TranService)ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
        //阶段和可能性的对应关系
        Map<String,String> pMap =(Map<String,String>)this.getServletContext().getAttribute("pMap");
        //将交易历史列表遍历
        for(TranHistory th:thList){
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id= request.getParameter("id");
        TranService ts =(TranService)ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.detail(id);
        //处理可能性
        String stage = t.getStage();
        Map<String,String> pMap =(Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility =pMap.get(stage);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("执行添加交易的操作");
        String id =UUIDUtil.getUUID();
        String owner =request.getParameter("owner");
        String money =request.getParameter("money");
        String name =request.getParameter("name");
        String expectedDate =request.getParameter("expectedDate");
        String customerName =request.getParameter("customerName");  //此时只有客户名称 ，没有id
        String stage =request.getParameter("stage");
        String  type =request.getParameter("type");
        String source =request.getParameter("source");
        String  activityId =request.getParameter("activityId");
        String contactsId =request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String  description =request.getParameter("description");
        String contactSummary =request.getParameter("contactSummary");
        String  nextContactTime =request.getParameter("nextContactTime");

        Tran t =new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setContactSummary(contactSummary);
        t.setSource(source);
        t.setStage(stage);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setNextContactTime(nextContactTime);
        t.setType(type);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        TranService ts =(TranService)ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t,customerName);
        if(flag){
            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得 客户名称列表（按照客户名称模糊查询）");
        String name = request.getParameter("name");
        CustomerService cs=(CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String>    sList = cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("开始执行交易列表的创建操作");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }
}
