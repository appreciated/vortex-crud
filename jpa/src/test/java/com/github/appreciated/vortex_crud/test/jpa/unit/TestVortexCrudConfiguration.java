package com.github.appreciated.vortex_crud.test.jpa.unit;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .build();
    }
}
