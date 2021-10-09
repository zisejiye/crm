package com.bipowernode.crm.settings.service.impl;

import com.bipowernode.crm.exception.LoginException;
import com.bipowernode.crm.settings.dao.UserDao;
import com.bipowernode.crm.settings.domain.User;
import com.bipowernode.crm.settings.service.UserService;
import com.bipowernode.crm.utils.DateTimeUtil;
import com.bipowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);



    public User login(String loginAct, String loginPwd, String ip) throws LoginException {


        Map<String,Object>  map = new HashMap<String, Object>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if(user == null){
            throw new LoginException("账号密码错误");
        }
        //程序运行到此说明账号密码正确，验证失效时间，ip地址、锁定状态
        String expireTime = user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账户已锁定");
        }
        //判断ip是否包含
        String allowIp = user.getAllowIps();
        if(!allowIp.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> uList = userDao.getUserList();
        return uList;
    }
}
