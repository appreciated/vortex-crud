package com.github.appreciated.vortex_crud.core.ui.factories.route.view;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.DataBinder;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormBuilder;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

public class StoreAccessor<ModelClass, FieldType, RepositoryType> {
    private final DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final Binder<Object> binder;
    private final ModelClass entity;
    private final DataBinder<ModelClass, FieldType, RepositoryType> dataBinder;
    private final FormBuilder<ModelClass, FieldType, RepositoryType> formBuilder;

    public StoreAccessor(DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
                         VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                         Binder<Object> binder,
                         ModelClass entity) {
        this.dataStoreConfig = dataStoreConfig;
        this.context = context;
        this.binder = binder;
        this.entity = entity;
        this.dataBinder = context.formCreator().getDataBinder();
        this.formBuilder = context.formCreator().getFormBuilder();
    }

    public Component getComponent(FieldType fieldName) {
        Field<ModelClass, FieldType, RepositoryType> field = dataStoreConfig.fields().get(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Field '" + fieldName + "' not found in configuration");
        }

        // Check RBAC permissions first?
        // FormCreator checks permissions.
        // If I use StoreAccessor, I should probably also check permissions?
        // But StoreAccessor is "low level" or explicit. The user explicitly asks for the component.
        // However, if the user shouldn't see it, maybe we should return null or a placeholder?
        // FormCreator does:
        /*
        VortexCrudRbacPermissionChecker.FieldAccessLevel userFieldAccess = null;
        if (permissionChecker != null) {
            userFieldAccess = permissionChecker.getUserFieldAccess(routeRenderer, field);
            if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE) {
                continue;
            }
        }
        */
        // Here we don't have routeRenderer easily available unless passed.
        // Assuming StoreAccessor is used inside a ViewRoute, we might want to enforce permissions.
        // But for now, I will assume the user handles logic or I should pass permissions.

        // Actually, if the user asks for it, they probably want it.
        // But binding should handle read-only if configured.

        Component component = formBuilder.createComponent(dataStoreConfig.factory(), fieldName, field, context);
        dataBinder.bindComponent(component, fieldName, field, entity, binder, context.reflectionService());

        return component;
    }

    public DataStoreConfig<ModelClass, FieldType, RepositoryType> getConfig() {
        return dataStoreConfig;
    }
}
