package com.github.appreciated.turbo_crud.ui.factories.route.form;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.FormConfiguration;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

/**
 * Default implementation of the {@link TurboCrudRouteFactory} interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class DefaultFormRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final FormCreator formCreator;
    private final TurboCrudRouteFactoryRegistry factoryRegistry;

    public DefaultFormRouteFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                       TurboCrudConfigService configService,
                                       FormCreator formCreator,
                                       TurboCrudRouteFactoryRegistry factoryRegistry
    ) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.formCreator = formCreator;
        this.factoryRegistry = factoryRegistry;
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            TurboCrudPathToRouteResolver routeResolver,
            boolean isWrapped,
            boolean hideHeader
    ) {
        Route route = routeResolver.getRouteForIndex(currentPathIndex);
        FormConfiguration formConfiguration = ConfigBeanFactory.create(route.getConfiguration(), FormConfiguration.class);
        return getForm(routeResolver, isWrapped, hideHeader, route, formConfiguration);
    }

    public VerticalLayout getForm(TurboCrudPathToRouteResolver routeResolver, boolean isWrapped, boolean hideHeader, Route route, FormConfiguration formConfiguration) {
        String table = route.getRepository();

        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(table);

        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");


        String lastSegment = routeResolver.getLastSegment();
        GenericEntity entity = entityManagerService.getRecordById(lastSegment);

        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);

        String prefix = !isWrapped ? layout.getTranslation(route.getTitle()) + " / " : "";

        binder.bind(
                titleComponent,
                entity1 -> prefix + entity1.getString(formConfiguration.getTitleField()),
                (entity1, string) -> {
                }
        );

        RepositoryConfig tables = configService.getConfiguration().getRepositoriesConfig().get(table);

        formCreator.bindAndAddToLayout(table, route, formConfiguration, entity,factoryRegistry, tables, binder, form, formCreator);

        binder.setBean(entity);

        // Generic Save button
        Button save = new Button(layout.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entityManagerService.updateRecordById(EntityUtil.getId(entity), entity);
                binder.setBean(entity);
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        save.addThemeVariants(LUMO_PRIMARY);

        // Generic Delete button
        Button delete = new Button(layout.getTranslation("button.delete.title"), event -> {
            entityManagerService.deleteRecordById(EntityUtil.getId(entity));
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
        });
        delete.addThemeVariants(LUMO_ERROR);

        // Generic Delete button
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
        headerBar.add(titleComponent, save, delete);
        headerBar.setAlignItems(CENTER);
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