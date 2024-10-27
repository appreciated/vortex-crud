package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.component.KanbanView;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nullable;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public class DefaultKanbanDetailFactoryImpl implements TurboCrudRouteFactory {
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudItemFactoryRegistry turboCrudItemFactory;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultKanbanDetailFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                          TurboCrudConfigService configService,
                                          TurboCrudItemFactoryRegistry turboCrudItemFactory,
                                          TurboCrudRouteFactoryRegistry routeFactory,
                                          FormCreator formCreator,
                                          TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                          TurboCrudIconFactory iconFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(route.getRepository());

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        Config factoryConfig = route.getConfiguration();
        KanbanConfig gridConfiguration = ConfigBeanFactory.create(factoryConfig, KanbanConfig.class);

        KanbanView kanbanView = new KanbanView(route.getRepository(), route, entityManagerService, routeFactory, turboCrudItemFactory, gridConfiguration, configService.getConfiguration(), dialogFactoryRegistry, formCreator);

        // Back button
        Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), event -> UI.getCurrent().getPage().getHistory().back());
        back.getStyle().set("font-size", "1.6em")
                .set("padding", "calc(var(--lumo-button-size) / 8)")
                .set("border-radius", "100%")
                .set("box-sizing", "content-box");


        // HEADER
        HorizontalLayout header = new RouteHeader(route, iconFactory);

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addClickListener(event -> onAdd());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout headerContainer = new HorizontalLayout(header, addButton);
        headerContainer.setPadding(false);
        headerContainer.setAlignItems(CENTER);
        headerContainer.setWidthFull();
        headerContainer.setJustifyContentMode(BETWEEN);

        assert detailRouteSetting != null;
        if (!detailRouteSetting.isWrapped()) {
            headerContainer.add(back);
        }

        if (!detailRouteSetting.isWrapped()) {
            headerContainer.add(back);
        }

        headerContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        if (!detailRouteSetting.isHeaderHidden()) {
            layout.add(headerContainer);
        }
        layout.add(kanbanView);
        layout.setPadding(true);
        layout.setSizeFull();
        return layout;
    }


    private void onAdd() {

    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
