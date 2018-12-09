package com.core.reflection.proxy;

import com.core.reflection.proxy.cglib.CGLibProxy;
import com.core.reflection.proxy.jdk.JDKProxy;

public class ProxyClient {
    public static void main(String[] args) {
        // 创建被代理对象
        IUserManager userManager = new UserManagerImpl();

        // 创建代理对象JDKProxy
        JDKProxy jdkProxy = new JDKProxy();
        IUserManager jdkUserManager = (IUserManager)jdkProxy.newProxy(userManager);
        jdkUserManager.addUser("1","Belly");

        // 创建代理对象CGLibProxy
        CGLibProxy cgLibProxy = new CGLibProxy();
        IUserManager cglibUserManager = (IUserManager) cgLibProxy.newProxy(userManager);
        cglibUserManager.addUser("2","Laura");
    }
}
