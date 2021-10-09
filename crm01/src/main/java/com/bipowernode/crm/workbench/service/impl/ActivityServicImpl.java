package com.bipowernode.crm.workbench.service.impl;

import com.bipowernode.crm.settings.dao.UserDao;
import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.utils.SqlSessionUtil;
import com.bipowernode.crm.vo.PageinationVo;
import com.bipowernode.crm.workbench.dao.ActivityDao;

import com.bipowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bipowernode.crm.workbench.domain.Activity;

import com.bipowernode.crm.workbench.domain.ActivityRemark;
import com.bipowernode.crm.workbench.service.ActivityServic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServicImpl implements ActivityServic {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity a) {
        boolean flag =true;
        int count = activityDao.save(a);
        if (count!=1) {
            flag =false;
        }
        return flag;
    }

    public PageinationVo<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total =activityDao.getTotalByCondition(map);
        //取得dataList
        List<Activity> dataList =activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中
        PageinationVo<Activity> vo = new PageinationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将Vo返回
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean  flag = true;
        //查出需要删除备注的数量
        int count1 =activityRemarkDao.getCountByAids(ids);
        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 =activityRemarkDao.deleteByAids(ids);
        if(count1!=count2){
            flag=false;
        }
        //删除市场活动
        int count3 =activityDao.delete(ids);
        if (count3!=ids.length) {
            flag = false;
        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        //取得uList
            List<User> uList = userDao.getUserList();
        //取a
          Activity a= activityDao.getById(id);
        //将uList和a 打包到Map中
        Map<String,Object>  map =new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("a",a);


        return map;
    }

    public boolean update(Activity a) {
        boolean flag =true;
        int count = activityDao.update(a);
        if (count!=1) {
            flag =false;
        }
        return flag;

    }

    public Activity detail(String id) {
        Activity a =activityDao.getDetail(id);
        return a  ;
    }



    public List<ActivityRemark>  getRemarkListAid(String activityId){
        List<ActivityRemark> aList=activityRemarkDao.getRemarkListAid(activityId);

        return aList;
    }

    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteById(id);
        if(count !=1){
            flag = false;
        }

        return flag;
    }

    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
         int count = activityRemarkDao.saveReamrk(ar);
         if (count!=1){
             flag=false;
        }
        return flag;
    }

    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);

        if (count != 1){
            flag =false;
        }

        return flag;
    }

    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList =activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    public List<Activity> getActivityListByNameAndNotClueId(Map<String, String> map) {
         List<Activity>  alist   =activityDao.getActivityListByNameAndNotClueId(map);
        return alist;
    }

    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList=activityDao.getActivityListByName(aname);

        return aList;
    }
}
