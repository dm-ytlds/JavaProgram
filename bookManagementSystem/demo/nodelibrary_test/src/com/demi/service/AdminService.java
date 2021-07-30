
package com.demi.service;

import com.demi.bean.Admin;


public interface AdminService {

    Admin get(String name);

    void save(Admin admin);

}
