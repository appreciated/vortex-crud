package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaVortexCrudConfigService implements VortexCrudConfigService<Object, String, String> {

    private final Application<Object, String, String> configuration;

    @Autowired
    public JpaVortexCrudConfigService(VortexCrudConfigurationProvider<Object, String, String> configurationProvider) {
        configuration =  configurationProvider.get();
    }

    public Application<Object, String, String> configuration() {
        return configuration;
    }

    public String applicationName() {
        return configuration.applicationName();
    }
}