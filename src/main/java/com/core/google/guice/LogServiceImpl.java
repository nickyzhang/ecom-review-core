package com.core.google.guice;

import java.time.LocalDate;

public class LogServiceImpl implements LogService {
    public void log(String msg) {
        System.out.println("[Info] ["+ LocalDate.now()+"]: " + msg);
    }
}
