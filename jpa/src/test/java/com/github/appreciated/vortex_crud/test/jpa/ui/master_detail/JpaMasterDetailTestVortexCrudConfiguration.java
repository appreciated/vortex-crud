package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMasterDetailTestRepository taskRepository;

    public JpaMasterDetailTestVortexCrudConfiguration(JpaMasterDetailTestRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                taskRepository, JpaDataStoreConfig.of(taskRepository)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "title", new JpaField(TextFieldFactory.class, true, true),
                                "status", new JpaField(TextFieldFactory.class, true)
                        ))
                        .build()
        );

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaRouteRenderer.of(SubmenuRouteFactory.class)
                .withDataStore(taskRepository)
                .withTitle("route.tasks.title")
                .withChildrenMap(Map.of(
                        "done", JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                                .withDataStore(taskRepository)
                                .withTitle("route.done-tasks.title")
                                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                                        .withTitleField("title")
                                        .build())
                                .build()
                ))
                .build());

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
