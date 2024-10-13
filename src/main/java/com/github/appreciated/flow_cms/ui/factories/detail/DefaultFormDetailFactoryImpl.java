package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.config.model.TableConfig;
import com.github.appreciated.flow_cms.entity.EntityUtil;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.components.H2WithHasValue;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
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
 * Default implementation of the EntityDetailRenderer interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class DefaultFormDetailFactoryImpl implements FlowCmsDetailFactory {

    private final FlowCmsEntityManagerService entityManagerService;
    private final FlowCmsConfigService configService;
    private final FormCreator formCreator;

    public DefaultFormDetailFactoryImpl(FlowCmsEntityManagerService entityManagerService,
                                        FlowCmsConfigService configService,
                                        FormCreator formCreator) {
        this.entityManagerService = entityManagerService;
        this.configService = configService;
        this.formCreator = formCreator;
    }

    @Override
    public Component renderDetail(String table,
                                  String title,
                                  DetailFactory detailFactory,
                                  GenericEntity entity,
                                  boolean isWrapped,
                                  boolean hideHeader,
                                  FlowCmsDetailFactoryRegistry detailFactoryRegistry) {
        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);

        String prefix = !isWrapped ? layout.getTranslation(title) + " / " : "";

        binder.bind(
                titleComponent,
                entity1 -> prefix + entity1.getString(detailFactory.getTitleColumn()),
                (entity1, string) -> {
                }
        );

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(table);

        formCreator.bindAndAddToLayout(table, detailFactory, entity, detailFactoryRegistry, tables, binder, form, formCreator);

        binder.setBean(entity);

        // Generic Save button
        Button saveButton = new Button(layout.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entityManagerService.updateRecordById(table, EntityUtil.getId(entity), entity);
                binder.setBean(entity);
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(LUMO_PRIMARY);

        // Generic Delete button
        Button deleteButton = new Button(layout.getTranslation("button.delete.title"), event -> {
            entityManagerService.deleteRecordById(table, EntityUtil.getId(entity));
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
        });
        deleteButton.addThemeVariants(LUMO_PRIMARY, LUMO_ERROR);

        // Add the form and buttons to the layout
        HorizontalLayout headerBar = new HorizontalLayout(titleComponent, saveButton, deleteButton);
        headerBar.setAlignItems(CENTER);
        if (!hideHeader) {
            layout.add(headerBar);
        }
        layout.add(form);
        layout.setPadding(true);
        return layout;
    }
}