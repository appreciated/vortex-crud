package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE;

@Service
public class JpaMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaMasterDetailTestRepository taskRepository;

    public JpaMasterDetailTestVortexCrudConfiguration(JpaMasterDetailTestRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(taskRepository)
                .title("route.projects.title-cards")
                .configuration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.images.labels.title").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreKey(taskRepository)
                .title("route.done-tasks.title")
                .configuration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField("title")
                        .descriptionField("description")
                        .build())
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
