package com.github.appreciated.turbo_crud.core.ui.factories.form;

import com.github.appreciated.turbo_crud.core.config.model.*;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.TurboCrudCollectionFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormCreator {

    private final DefaultFieldFactoryRegistry componentFactory;
    private final TurboCrudCollectionFactoryRegistry collectionFactoryRegistry;

    public FormCreator(DefaultFieldFactoryRegistry componentFactory, TurboCrudCollectionFactoryRegistry collectionFactoryRegistry) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
    }

    public <DataStoreId, FieldId> void bindAndAddToLayout(DataStoreId table,
                                                          Route<DataStoreId, FieldId> route,
                                                          RouteConfiguration<DataStoreId, FieldId> formConfig,
                                                          GenericEntity entity,
                                                          TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                                                          DataStoreConfig<FieldId> tables,
                                                          Binder<GenericEntity> binder,
                                                          FormLayout form,
                                                          FormCreator formCreator) {
        Map<FieldId, Field> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId> element : formConfig.getChildren()) {
            FieldId fieldName = element.getField();
            if (!element.getType().equals("collection")) {
                Field field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
                }
                TurboCrudFieldFactory<DataStoreId, FieldId> factory = componentFactory.getFactory(field.getFactory());
                Component component = factory.createComponent(table, fieldName, field);
                //TODO Add Factory to generate field from FieldId
                binder.bind((HasValue) component, entity1 -> entity1.get((String) fieldName), (entity1, o) -> entity1.put((String) fieldName, o));
                if (component instanceof HasSize) {
                    ((HasSize) component).setWidthFull();
                }
                if (component instanceof HasLabel) {
                    ((HasLabel) component).setLabel(component.getTranslation(element.getLabel()));
                    form.add(component);
                    form.setColspan(component, (element.getSpan() == null ? 1 : element.getSpan()));
                } else {
                    FormLayout.FormItem formItem = form.addFormItem(component, component.getTranslation(element.getLabel()));
                    form.setColspan(formItem, (element.getSpan() == null ? 1 : element.getSpan()));
                }
            } else {
                if (element.getType().equals("collection")) {
                    Component collection = collectionFactoryRegistry.getFactory(element.getFactory()).createCollection(
                            DataStoreUtil.getId(entity),
                            route,
                            element,
                            routeFactory,
                            formCreator
                    );
                    form.add(collection);
                    form.setColspan(collection, (element.getSpan() == null ? 2 : element.getSpan()));
                } else {
                    throw new IllegalStateException("Cannot initialize element with name '%s'".formatted(fieldName));
                }
            }
        }
    }
}