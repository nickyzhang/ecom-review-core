package com.core.google.guice;

import com.google.inject.Inject;

public class MobileSide implements Application {
    private UserService userService;
    private LogService logService;

    @Inject
    public MobileSide(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public void work() {
        boolean success = userService.login("admin","123abcABC");
        if (success)
            logService.log("admin user success to login!");
        else
            logService.log("admin user fail to login!");
    }

}
