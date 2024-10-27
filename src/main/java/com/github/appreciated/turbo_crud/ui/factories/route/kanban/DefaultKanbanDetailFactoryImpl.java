package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.component.KanbanView;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nullable;

public class DefaultKanbanDetailFactoryImpl implements TurboCrudRouteFactory {
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudItemFactoryRegistry turboCrudItemFactory;

    public DefaultKanbanDetailFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                          TurboCrudConfigService configService,
                                          TurboCrudItemFactoryRegistry turboCrudItemFactory,
                                          FormCreator formCreator) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
    }


    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(route.getRepository());

        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        RepositoryConfig tables = configService.getConfiguration().getRepositoriesConfig().get(route.getRepository());

        Config factoryConfig = route.getConfiguration();
        KanbanConfig gridConfiguration = ConfigBeanFactory.create(factoryConfig, KanbanConfig.class);

        KanbanView kanbanView = new KanbanView(route.getRepository(),entityManagerService, turboCrudItemFactory, gridConfiguration, configService.getConfiguration());

        // Back button
        Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), event -> UI.getCurrent().getPage().getHistory().back());
        back.getStyle().set("font-size", "1.6em")
                .set("padding", "calc(var(--lumo-button-size) / 8)")
                .set("border-radius", "100%")
                .set("box-sizing", "content-box");

        HorizontalLayout headerBar = new HorizontalLayout();
        assert detailRouteSetting != null;
        if (!detailRouteSetting.isWrapped()) {
            headerBar.add(back);
        }

        // Add the form and buttons to the layout
        headerBar.add(titleComponent);
        headerBar.setAlignItems(FlexComponent.Alignment.CENTER);
        if (!detailRouteSetting.isHeaderHidden()) {
            layout.add(headerBar);
        }
        layout.add(kanbanView);
        layout.setPadding(true);
        layout.setSizeFull();
        return layout;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
