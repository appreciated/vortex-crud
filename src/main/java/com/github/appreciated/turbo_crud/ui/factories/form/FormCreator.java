package com.github.appreciated.turbo_crud.ui.factories.form;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.TurboCrudCollectionFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class FormCreator {

    private final DefaultFieldFactoryRegistryImpl componentFactory;
    private final TurboCrudCollectionFactoryRegistry collectionFactoryRegistry;

    public FormCreator(DefaultFieldFactoryRegistryImpl componentFactory, TurboCrudCollectionFactoryRegistry collectionFactoryRegistry) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
    }

    public void bindAndAddToLayout(String table,
                                   Route route,
                                   FormConfiguration formConfiguration,
                                   GenericEntity entity,
                                   TurboCrudRouteFactoryRegistry routeFactory,
                                   TableConfig tables,
                                   Binder<GenericEntity> binder,
                                   FormLayout form,
                                   FormCreator formCreator) {
        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormItem field : formConfiguration.getChildren()) {
            String fieldName = field.getColumn();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);
            if (fieldConfig == null && field.getFactory() != null && !field.getType().equals("collection")) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            if (fieldConfig != null && !field.getType().equals("collection")) {
                TurboCrudFieldFactory factory = componentFactory.getFactory(fieldConfig.getFactory());
                Component component = factory.createComponent(table, fieldName, fieldConfig);
                if (component instanceof HasLabel) {
                    ((HasLabel) component).setLabel(component.getTranslation(field.getLabel()));
                }
                binder.bind((HasValue) component, entity1 -> entity1.get(fieldName), (entity1, o) -> entity1.put(fieldName, o));
                form.add(component);
                form.setColspan(component, (field.getSpan() == null ? 1 : field.getSpan()));
            } else {
                Component collection = collectionFactoryRegistry.getFactory(field.getFactory()).createCollection(
                        EntityUtil.getId(entity),
                        route,
                        field,
                        routeFactory,
                        formCreator
                );
                form.add(collection);
                form.setColspan(collection, (field.getSpan() == null ? 2 : field.getSpan()));
            }
        }
    }
}