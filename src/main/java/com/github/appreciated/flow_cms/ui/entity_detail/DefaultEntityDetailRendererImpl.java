package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.*;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.components.DefaultFlowCmsComponentFactoryImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the EntityDetailRenderer interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class DefaultEntityDetailRendererImpl implements EntityDetailRenderer {

    private final DefaultFlowCmsComponentFactoryImpl componentFactory;
    private final DynamicEntityManagerService entityManagerService;
    private final FlowCmsConfigService cmsConfigService;

    public DefaultEntityDetailRendererImpl(DefaultFlowCmsComponentFactoryImpl componentFactory, DynamicEntityManagerService entityManagerService, FlowCmsConfigService cmsConfigService) {
        this.componentFactory = componentFactory;
        this.entityManagerService = entityManagerService;
        this.cmsConfigService = cmsConfigService;
    }

    @Override
    public Component renderDetail(RouteConfig routeConfig, GenericEntity entity) {
        String table = routeConfig.getTable();

        TableConfig tables = cmsConfigService.getConfiguration().getTablesConfig().get(routeConfig.getTable());

        VerticalLayout layout = new VerticalLayout();
        FormLayout form = new FormLayout();
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        DetailRenderer itemRendererConfig = routeConfig.getRender_configuration().getDetail_renderer();

        Map<String, Component> fieldComponents = new HashMap<>();

        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormField field : itemRendererConfig.getChildren()) {
            String fieldName = field.getField();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);

            Component component = componentFactory.createComponent(fieldConfig);
            binder.bind((HasValue) component, entity1 -> entity1.get(fieldName), (entity1, o) -> entity1.put(fieldName, o));

            form.add(component);
            fieldComponents.put(fieldName, component);
        }

        // Save button
        Button saveButton = new Button("Save", event -> {
            try {
                binder.writeBean(entity);
                entityManagerService.updateRecordById(table, entity.get("id"), entity);
                Div notification = new Div();
                notification.setText("Entity saved successfully.");
                layout.add(notification);
            } catch (ValidationException e) {
                Div errorNotification = new Div();
                errorNotification.setText("Failed to save entity: " + e.getMessage());
                layout.add(errorNotification);
            }
        });

        // Delete button
        Button deleteButton = new Button("Delete", event -> {
            entityManagerService.deleteRecordById(table, entity.get("id"));
            Div notification = new Div();
            notification.setText("Entity deleted successfully.");
            layout.add(notification);
        });

        // Add the form and buttons to the layout
        layout.add(form, saveButton, deleteButton);
        return layout;
    }
}