package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.*;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.fields.DefaultFlowCmsFieldFactoryImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.InputField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.Map;

/**
 * Default implementation of the EntityDetailRenderer interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class DefaultFormEntityDetailRendererImpl implements FlowCmsEntityDetailRenderer {

    private final DefaultFlowCmsFieldFactoryImpl componentFactory;
    private final DynamicEntityManagerService entityManagerService;
    private final FlowCmsConfigService cmsConfigService;

    public DefaultFormEntityDetailRendererImpl(DefaultFlowCmsFieldFactoryImpl componentFactory, DynamicEntityManagerService entityManagerService, FlowCmsConfigService cmsConfigService) {
        this.componentFactory = componentFactory;
        this.entityManagerService = entityManagerService;
        this.cmsConfigService = cmsConfigService;
    }

    @Override
    public Component renderDetail(RouteConfig routeConfig, GenericEntity entity) {
        String table = routeConfig.getTable();

        TableConfig tables = cmsConfigService.getConfiguration().getTablesConfig().get(routeConfig.getTable());

        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);

        binder.bind(
                titleComponent,
                entity1 -> layout.getTranslation(routeConfig.getTitle()) + "/" + entity1.getString(routeConfig.getRenderConfiguration().getDetailRenderer().getTitleField()),
                (entity1, string) -> {
                }
        );

        DetailRenderer itemRendererConfig = routeConfig.getRenderConfiguration().getDetailRenderer();

        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormField field : itemRendererConfig.getChildren()) {
            String fieldName = field.getField();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);

            Component component = componentFactory.createComponent(table, fieldName, fieldConfig);
            if (component instanceof InputField) {
                ((InputField<?, ?>) component).setLabel(component.getTranslation(field.getLabel()));
            }
            binder.bind((HasValue) component, entity1 -> entity1.get(fieldName), (entity1, o) -> entity1.put(fieldName, o));

            form.add(component);
        }

        binder.setBean(entity);

        // Save button
        Button saveButton = new Button(layout.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entityManagerService.updateRecordById(table, entity.get("id"), entity);
                binder.setBean(entity);
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // Delete button
        Button deleteButton = new Button(layout.getTranslation("button.delete.title"), event -> {
            entityManagerService.deleteRecordById(table, entity.get("id"));
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
        });

        // Add the form and buttons to the layout
        layout.add(new HorizontalLayout(titleComponent, saveButton, deleteButton), form);
        return layout;
    }
}