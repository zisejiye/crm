package com.bipowernode.crm.workbench.service;

import com.bipowernode.crm.vo.PageinationVo;
import com.bipowernode.crm.workbench.domain.Activity;
import com.bipowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityServic {
    boolean save(Activity a);

    PageinationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
