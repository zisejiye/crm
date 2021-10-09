package com.bipowernode.crm.workbench.dao;

import com.bipowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListAid(String activityId);

    int deleteById(String id);

    int saveReamrk(ActivityRemark ar);


    int updateRemark(ActivityRemark ar);
}
