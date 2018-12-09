package com.core.google.guice;

public class UserImpl implements UserService {
    public boolean login(String username, String password) {

        return "admin".equals(username) && "123abcABC".equals(password);
    }
}
