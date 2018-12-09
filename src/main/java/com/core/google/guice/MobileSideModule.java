package com.core.google.guice;

import com.google.inject.AbstractModule;

public class MobileSideModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LogService.class).to(LogServiceImpl.class);
        bind(UserService.class).to(UserImpl.class);
        bind(Application.class).to(MobileSide.class);
    }
}
