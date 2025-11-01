package com.github.appreciated.vortex_crud.test.jpa.ui.subroute;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaRouteRenderer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaSubrouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaSubrouteTestRepository taskRepository;

    public JpaSubrouteTestVortexCrudConfiguration(JpaSubrouteTestRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                taskRepository, JpaDataStoreConfig.of(taskRepository)
                        .fields(Map.of(
                                "id", new IdField<>(),
                                "title", new TextField<>(),
                                "url", new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaRouteRenderer.of(SubmenuRouteFactory.class)
                .dataStoreKey(taskRepository)
                .title("route.tasks.title")
                .childrenMap(Map.of(
                        "open", JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                                .dataStoreKey(taskRepository)
                                .title("route.open-tasks.title")
                                .configuration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                                        .titleField("title")
                                        .build())
                                .build()
                ))
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
