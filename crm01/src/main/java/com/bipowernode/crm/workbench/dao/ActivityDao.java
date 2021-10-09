package com.bipowernode.crm.workbench.dao;

import com.bipowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {


    int save(Activity a);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int delete(String[] ids);


    Activity getById(String id);

    int update(Activity a);

    Activity getDetail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
