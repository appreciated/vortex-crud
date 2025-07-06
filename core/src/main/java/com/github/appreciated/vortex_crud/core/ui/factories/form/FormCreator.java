package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
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
public class FormCreator<DataStoreId, FieldId, ModelClass> {

    private final DefaultFieldFactoryRegistry<DataStoreId, FieldId, ModelClass> componentFactory;
    private final VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, ModelClass> collectionFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public FormCreator(DefaultFieldFactoryRegistry<DataStoreId, FieldId, ModelClass> componentFactory,
                       VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, ModelClass> collectionFactoryRegistry,
                       VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    public void bindAndAddToLayout(DataStoreId table,
                                                          RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer,
                                                          RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> formConfig,
                                                          ModelClass entity,
                                                          VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory,
                                                          DataStoreConfig<DataStoreId, FieldId, ModelClass> tables,
                                                          Binder<ModelClass> binder,
                                                          FormLayout form,
                                                          FormCreator<DataStoreId, FieldId, ModelClass> formCreator) {
        Map<FieldId, Field<DataStoreId, FieldId, ModelClass>> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId, ModelClass> element : formConfig.getChildren()) {
            FieldId fieldName = element.getField();
            if (!element.getType().equals("collection")) {
                Field<DataStoreId, FieldId, ModelClass> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + table + "'");
                }
                VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass> factory = componentFactory.getFactory(field.getFactory());
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