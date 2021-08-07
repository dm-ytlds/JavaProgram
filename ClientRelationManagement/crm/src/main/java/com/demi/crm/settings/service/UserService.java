package com.demi.crm.settings.service;

import com.demi.crm.exception.LoginException;
import com.demi.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
