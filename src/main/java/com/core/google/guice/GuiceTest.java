package com.core.google.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceTest {

    private static Injector injector = Guice.createInjector(new MobileSideModule());


    public static void main(String[] args) {
        Application app = injector.getInstance(Application.class);
        app.work();
    }
}
