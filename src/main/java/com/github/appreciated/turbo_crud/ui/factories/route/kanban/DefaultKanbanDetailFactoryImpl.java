package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.config.model.TableConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
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
    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudConfigService configService;
    private final FormCreator formCreator;

    public DefaultKanbanDetailFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                          TurboCrudConfigService configService,
                                          FormCreator formCreator) {
        this.entityManagerService = entityManagerService;
        this.configService = configService;
        this.formCreator = formCreator;
    }


    public Component renderRoute(
            Integer currentPathIndex,
            TurboCrudPathToRouteResolver pathVariables,
            boolean isWrapped,
            boolean hideHeader
    ) {
        Route route = pathVariables.getRouteForIndex(currentPathIndex);

        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(route.getTable());

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
