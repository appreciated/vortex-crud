package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormCreator<DataStoreId, FieldId> {

    private final DefaultFieldFactoryRegistry<DataStoreId, FieldId> componentFactory;
    private final VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId> collectionFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public FormCreator(DefaultFieldFactoryRegistry<DataStoreId, FieldId> componentFactory,
                       VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId> collectionFactoryRegistry,
                       VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    public void bindAndAddToLayout(DataStoreId table,
                                                          RouteRenderer<DataStoreId, FieldId> routeRenderer,
                                                          RouteRendererConfiguration<DataStoreId, FieldId> formConfig,
                                                          GenericEntity entity,
                                                          VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                                                          DataStoreConfig<DataStoreId, FieldId> tables,
                                                          Binder<GenericEntity> binder,
                                                          FormLayout form,
                                                          FormCreator<DataStoreId, FieldId> formCreator) {
        Map<FieldId, Field<DataStoreId, FieldId>> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId> element : formConfig.getChildren()) {
            FieldId fieldName = element.getField();
            if (!element.getType().equals("collection")) {
                Field<DataStoreId, FieldId> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + table + "'");
                }
                VortexCrudFieldFactory<DataStoreId, FieldId> factory = componentFactory.getFactory(field.getFactory());
                Component component = factory.createComponent(table, fieldName, field);
                binder.bind(
                        (HasValue) component,
                        entity1 -> entity1.get(fieldNameResolver.getKeyForFieldId(fieldName)),
                        (entity1, o) -> entity1.put(fieldNameResolver.getKeyForFieldId(fieldName), o)
                );
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
                            routeRenderer,
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