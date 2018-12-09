package com.core.tools;

import com.robert.vesta.service.impl.IdServiceImpl;
import com.robert.vesta.service.impl.provider.PropertyMachineIdProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

public class IdGeneratorConfig {

    @Autowired
    Environment environment;

    @Bean
    public IdServiceImpl idService(){
        IdServiceImpl idService = new IdServiceImpl();
        idService.setMachineIdProvider(propertyMachineIdProvider());
        idService.init();
        return idService;
    }

    @Bean
    public PropertyMachineIdProvider propertyMachineIdProvider() {
        PropertyMachineIdProvider machineIdProvider = new PropertyMachineIdProvider();
        if (StringUtils.isBlank("1")) {
            machineIdProvider.setMachineId(1L);
            return machineIdProvider;
        }
        machineIdProvider.setMachineId(Long.valueOf("1"));
        return machineIdProvider;
    }

}
