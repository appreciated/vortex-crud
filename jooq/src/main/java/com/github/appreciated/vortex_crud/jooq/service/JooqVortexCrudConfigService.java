package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqVortexCrudConfigService implements VortexCrudConfigService<Class<? extends TableRecord<?>>, TableField<?, ?>> {

    private final Application<Class<? extends TableRecord<?>>, TableField<?, ?>> configuration;

    @Autowired
    public JooqVortexCrudConfigService(VortexCrudConfigurationProvider<Class<? extends TableRecord<?>>, TableField<?, ?>> configurationProvider) {
        configuration = configurationProvider.get();
    }

    public Application<Class<? extends TableRecord<?>>, TableField<?, ?>> getConfiguration() {
        return configuration;
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
