package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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
import com.vaadin.flow.data.validator.BeanValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormCreator<DataStoreId, FieldId, KeyType> {

    private final DefaultFieldFactoryRegistry<DataStoreId, FieldId, KeyType> componentFactory;
    private final VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, KeyType> collectionFactoryRegistry;
    private final ReflectionService<FieldId> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public FormCreator(DefaultFieldFactoryRegistry<DataStoreId, FieldId, KeyType> componentFactory,
                       VortexCrudCollectionFactoryRegistry<DataStoreId, FieldId, KeyType> collectionFactoryRegistry,
                       ReflectionService<FieldId> reflectionService,
                       VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.componentFactory = componentFactory;
        this.collectionFactoryRegistry = collectionFactoryRegistry;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
    }

    public void bindAndAddToLayout(KeyType dataStoreKey,
                                   RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                                   RouteRendererConfiguration<DataStoreId, FieldId, KeyType> formConfig,
                                   Object entity,
                                   VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                                   DataStoreConfig<DataStoreId, FieldId, KeyType> tables,
                                   Binder<Object> binder,
                                   FormLayout form,
                                   FormCreator<DataStoreId, FieldId, KeyType> formCreator) {
        Map<FieldId, Field<DataStoreId, FieldId, KeyType>> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId, KeyType> element : formConfig.getChildren()) {
            FieldId fieldName = element.getField();
            if (!element.getType().equals("collection")) {
                Field<DataStoreId, FieldId, KeyType> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + dataStoreKey + "'");
                }
                VortexCrudFieldFactory<DataStoreId, FieldId, KeyType> factory = componentFactory.getFactory(field.getFactory());
                Component component = factory.createComponent(dataStoreKey, fieldName, field);

                // Apply validation if present
                if (field.getValidation() != null) {
                    field.getValidation().applyToComponent(component);
                }

                Binder.BindingBuilder<Object, Object> builder = (Binder.BindingBuilder<Object, Object>) binder.forField((HasValue<?, ?>) component);
                if (fieldName instanceof String propertyName) {
                    builder = builder.withValidator(new BeanValidator(entity.getClass(), propertyName));
                }
                if (field.isRequired() && component instanceof HasValue) {
                    builder = builder.asRequired();
                }

                builder.bind(
                        entity1 -> reflectionService.getValue(entity1, fieldName),
                        (entity1, o) -> reflectionService.setValue(entity1, fieldName, o)
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
                            reflectionService.getId(entity),
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