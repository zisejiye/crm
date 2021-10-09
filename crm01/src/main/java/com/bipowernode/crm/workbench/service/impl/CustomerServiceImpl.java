package com.bipowernode.crm.workbench.service.impl;

import com.bipowernode.crm.utils.SqlSessionUtil;
import com.bipowernode.crm.workbench.dao.CustomerDao;
import com.bipowernode.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {
        List<String> sList=customerDao.getCustomerName(name);
        return sList;
    }
}
