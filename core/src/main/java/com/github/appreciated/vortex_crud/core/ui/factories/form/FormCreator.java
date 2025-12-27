package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FormCreator<ModelClass, FieldType, RepositoryType> {

    private final FormBuilder<ModelClass, FieldType, RepositoryType> formBuilder;
    private final DataBinder<ModelClass, FieldType, RepositoryType> dataBinder;
    private final SecurityEnforcer<ModelClass, FieldType, RepositoryType> securityEnforcer;

    @Autowired
    public FormCreator(FormBuilder<ModelClass, FieldType, RepositoryType> formBuilder,
                       DataBinder<ModelClass, FieldType, RepositoryType> dataBinder,
                       SecurityEnforcer<ModelClass, FieldType, RepositoryType> securityEnforcer) {
        this.formBuilder = formBuilder;
        this.dataBinder = dataBinder;
        this.securityEnforcer = securityEnforcer;
    }

    public void bindAndAddToLayout(RepositoryType dataStoreKey,
                                   RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                   List<InternalFormElement<ModelClass, FieldType, RepositoryType>> fieldsViewConfig,
                                   Object entity,
                                   VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                   DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
                                   Binder<Object> binder,
                                   FormLayout form) {
        Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = dataStoreConfig.fields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> element : fieldsViewConfig) {
            if (element.type() != ViewFieldType.COLLECTION) {
                FieldType fieldName = element.field();
                Field<ModelClass, FieldType, RepositoryType> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + dataStoreKey + "'");
                }

                // Check RBAC permissions first
                VortexCrudRbacPermissionChecker.FieldAccessLevel userFieldAccess = securityEnforcer.checkAccess(routeRenderer, field);
                if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE) {
                    continue;
                }

                Component component = formBuilder.createComponent(dataStoreKey, fieldName, field, context);

                dataBinder.bindComponent(component, fieldName, field, entity, binder, context.reflectionService());

                // Apply RBAC field-level permissions AFTER binding
                securityEnforcer.applyReadOnly(component, userFieldAccess);

                formBuilder.addComponentToForm(component, element, form);
            } else {
                formBuilder.createAndAddCollectionToForm(routeRenderer, element, entity, context, form);
            }
        }
    }
}
