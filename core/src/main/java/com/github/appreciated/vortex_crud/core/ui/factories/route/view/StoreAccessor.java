package com.github.appreciated.vortex_crud.core.ui.factories.route.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

public class StoreAccessor<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final DataStoreConfig<ModelClass, FieldType, RepositoryType> config;
    private final Binder<Object> binder;
    private final ModelClass entity;

    public StoreAccessor(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                         DataStoreConfig<ModelClass, FieldType, RepositoryType> config,
                         Binder<Object> binder,
                         ModelClass entity) {
        this.context = context;
        this.config = config;
        this.binder = binder;
        this.entity = entity;
    }

    public Component getComponent(FieldType fieldType) {
        Field<ModelClass, FieldType, RepositoryType> fieldConfig = config.fields().get(fieldType);
        if (fieldConfig == null) {
            throw new IllegalArgumentException("Field not found: " + fieldType);
        }

        Component component = context.formCreator().formBuilder().createComponent(config.factory(), fieldType, fieldConfig, context);
        context.formCreator().dataBinder().bindComponent(component, fieldType, fieldConfig, entity, binder, context.reflectionService());

        return component;
    }
}
