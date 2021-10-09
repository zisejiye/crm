package com.bipowernode.crm.workbench.service.impl;

import com.bipowernode.crm.utils.DateTimeUtil;
import com.bipowernode.crm.utils.SqlSessionUtil;
import com.bipowernode.crm.utils.UUIDUtil;
import com.bipowernode.crm.workbench.dao.CustomerDao;
import com.bipowernode.crm.workbench.dao.TranDao;
import com.bipowernode.crm.workbench.dao.TranHistoryDao;
import com.bipowernode.crm.workbench.domain.Customer;
import com.bipowernode.crm.workbench.domain.Tran;
import com.bipowernode.crm.workbench.domain.TranHistory;
import com.bipowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public boolean save(Tran t, String customerName) {
        /* 交易添加业务
           参数t里面缺少了客户的主键，CustomerId
           先处理客户的需求
            （1）判断CustomerName，根据客户名在客户列表精确查询
                  如果客户存在，提取客户id，封装到t对象中去
                  客户若不存在，则客户表新建一条客户信息，将新建的客户封装到t对象中
             （2）t对象补全后，需执行添加交易的操作

             （3）添加交易完毕后创建一个交易历史
        * */
        boolean flag = true;
        Customer cus =customerDao.getCustomerByName(customerName);
        //客户不存在，创建一条客户信息，
        if (cus==null){
             cus=new Customer();
             cus.setId(UUIDUtil.getUUID());
             cus.setName(customerName);
             cus.setCreateBy(t.getCreateBy());
             cus.setCreateTime(DateTimeUtil.getSysTime());
             cus.setContactSummary(t.getContactSummary());
             cus.setNextContactTime(t.getNextContactTime());
             cus.setOwner(t.getOwner());
             //添加客户
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag=false;
            }
        }
        //现在客户已经存在，客户id存在，将客户id封装到t对象中
        t.setCustomerId(cus.getId());

        //添加交易
        int count2  =tranDao.save(t);
        if (count2!=1){
            flag=false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getCreateTime());
        th.setCreateBy(t.getCreateBy());
        int count3=tranHistoryDao.save(th);
        if (count3!=1){
            flag=false;
        }
        return flag;
    }

    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList= tranHistoryDao.getHistoryListByTranId(tranId);
        return thList;
    }

    public boolean changeStage(Tran t) {
        boolean flag = true;
        //改变交易阶段
        int count1=tranDao.changeStage(t);
        if (count1!=1){
            flag=false;
        }
        //交易阶段改变后，改变交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag=false;
        }

        return flag;
    }

    public Map<String, Object> getCharts() {
        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList=tranDao.getCharts();

        //将total和dataList保存到Map中
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);


        //返回map
        return map;
    }
}
