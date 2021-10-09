package com.bipowernode.crm.settings.service;

import com.bipowernode.crm.exception.LoginException;
import com.bipowernode.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;


    List<User> getUserList();
}
