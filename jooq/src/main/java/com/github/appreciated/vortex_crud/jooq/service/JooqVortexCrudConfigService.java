package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqVortexCrudConfigService implements VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration;

    @Autowired
    public JooqVortexCrudConfigService(VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configurationProvider) {
        configuration = configurationProvider.get();
    }

    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        return configuration;
    }

    public String applicationName() {
        return configuration.applicationName();
    }
}
