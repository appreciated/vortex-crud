package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.component.KanbanView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultKanbanDetailFactoryImpl implements TurboCrudRouteFactory {
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;

    public DefaultKanbanDetailFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                          TurboCrudConfigService configService,
                                          FormCreator formCreator) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
    }


    public Component renderRoute(
            Integer currentPathIndex,
            TurboCrudPathToRouteResolver routeResolver,
            boolean isWrapped,
            boolean hideHeader
    ) {
        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(route.getRepository());

        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");

        RepositoryConfig tables = configService.getConfiguration().getRepositoriesConfig().get(route.getRepository());

        KanbanView kanbanView = new KanbanView();

        // Back button
        Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), event -> UI.getCurrent().getPage().getHistory().back());
        back.getStyle().set("font-size", "1.6em")
                .set("padding", "calc(var(--lumo-button-size) / 8)")
                .set("border-radius", "100%")
                .set("box-sizing", "content-box");

        HorizontalLayout headerBar = new HorizontalLayout();
        if (!isWrapped) {
            headerBar.add(back);
        }

        // Add the form and buttons to the layout
        headerBar.add(titleComponent);
        headerBar.setAlignItems(FlexComponent.Alignment.CENTER);
        if (!hideHeader) {
            layout.add(headerBar);
        }
        layout.add(form);
        layout.setPadding(true);
        return layout;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
