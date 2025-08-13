package com.github.appreciated.vortex_crud.test.jpa.ui.i18n;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaI18nTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> app = new Application<>() {};
        app.setName("I18N Test");
        app.setI18nBundlePrefix("some_i18n");
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = new LinkedHashMap<>();
        app.setDataStores(dataStores);
        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        app.setRouteRenderers(routes);
        return app;
    }
}
