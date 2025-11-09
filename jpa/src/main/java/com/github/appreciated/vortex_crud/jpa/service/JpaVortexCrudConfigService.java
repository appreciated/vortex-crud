package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaDataStoreFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class JpaVortexCrudConfigService implements VortexCrudConfigService<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration;

    @Autowired
    public JpaVortexCrudConfigService(VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configurationProvider, JpaDataStoreFactoryRegistry registry) {
        configuration =  configurationProvider.get().toBuilder().dataStores(registry.getDataStores()).build();
    }

    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration() {
        return configuration;
    }

    public String getApplicationName() {
        return configuration.applicationName();
    }
}
