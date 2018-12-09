package com.core.reflection.proxy;

import com.core.reflection.access.Model;

public class UserManagerImpl implements IUserManager {

    public void addUser(String userId, String userName) {
        System.out.println("[被代理实现类]：用户名： "+userName);
    }
}
