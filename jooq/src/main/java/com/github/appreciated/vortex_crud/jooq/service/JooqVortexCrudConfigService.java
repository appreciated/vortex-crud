package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqVortexCrudConfigService implements VortexCrudConfigService<Table<?>, TableField<?, ?>> {

    private final Application<Table<?>, TableField<?, ?>> configuration;

    @Autowired
    public JooqVortexCrudConfigService(VortexCrudConfigurationProvider<Table<?>, TableField<?, ?>> configurationProvider) {
        configuration = configurationProvider.get();
    }

    public Application<Table<?>, TableField<?, ?>> getConfiguration() {
        return configuration;
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
