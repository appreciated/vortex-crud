package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaKanbanTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaKanbanTestRepository taskRepository;

    public JpaKanbanTestVortexCrudConfiguration(JpaKanbanTestRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {

        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(taskRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(new JpaFieldElement("title", "route.tasks.labels.title"))
                        .build())
                .build();

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("a", "enums.option1");
        enumOptions.put("b", "enums.option2");
        enumOptions.put("c", "enums.option3");

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JpaRouteRenderer.of(KanbanDetailFactory.class)
                .withIconFactory(VaadinIcon.TASKS::create)
                .withDataStore(taskRepository)
                .withTitle("route.open-tasks.title")
                .withConfiguration(JpaKanban.of(CardFactory.class)
                        .withTitleField("title")
                        .withDescriptionField("description")
                        .withColumnField("status")
                        .withRowIndexField("rowIndex")
                        .withFilterField("title")
                        .build())
                .withChild(taskForm)
                .build()
        );

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withSelects(Selects.of().withConfigs(Map.of("enum-options", enumOptions)).build())
                .build();
    }
}
