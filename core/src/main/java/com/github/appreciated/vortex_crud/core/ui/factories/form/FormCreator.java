package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.components.NumericIdTextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FormCreator<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;

    public FormCreator(@Autowired(required = false) VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker) {
        this.permissionChecker = permissionChecker;
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
        ReflectionService<FieldType> reflectionService = context.reflectionService();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> element : fieldsViewConfig) {
            if (element.type() != ViewFieldType.COLLECTION) {
                FieldType fieldName = element.field();
                Field<ModelClass, FieldType, RepositoryType> field = fieldsConfig.get(fieldName);
                if (field == null) {
                    throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + dataStoreKey + "'");
                }

                VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = field.factory();
                Component component = factory.createComponent(dataStoreKey, fieldName, field, context);

                // Check RBAC permissions first but apply readonly AFTER binding
                VortexCrudRbacPermissionChecker.FieldAccessLevel userFieldAccess = null;
                if (permissionChecker != null) {
                    userFieldAccess = permissionChecker.getUserFieldAccess(routeRenderer, field);
                    if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE) {
                        continue;
                    }
                }

                // Handle NumericIdTextField with converter
                if (component instanceof NumericIdTextField) {
                    Binder.BindingBuilder<Object, String> builder = binder.forField((HasValue<?, String>) component);

                    Converter<String, Object> numericConverter = new Converter<String, Object>() {
                        @Override
                        public Result<Object> convertToModel(String value, com.vaadin.flow.data.binder.ValueContext context) {
                            // Read-only field, conversion to model not needed
                            return Result.ok(value);
                        }

                        @Override
                        public String convertToPresentation(Object value, com.vaadin.flow.data.binder.ValueContext context) {
                            return value == null ? "" : value.toString();
                        }
                    };

                    builder.withConverter(numericConverter).bind(
                            entity1 -> reflectionService.getValue(entity1, fieldName),
                            (entity1, o) -> {} // Read-only, no need to set
                    );
                } else {
                    // Standard binding for other components
                    Binder.BindingBuilder<Object, Object> builder = (Binder.BindingBuilder<Object, Object>) binder.forField((HasValue<?, ?>) component);

                    if (fieldName instanceof String propertyName) {
                        builder = builder.withValidator(new BeanValidator(entity.getClass(), propertyName));
                    }
                    if (field.required() && component instanceof HasValue) {
                        builder = builder.asRequired();
                    }

                    // Apply custom validators if present
                    if (field.validators() != null && !field.validators().isEmpty()) {
                        for (Validator<?> validator : field.validators()) {
                            builder = builder.withValidator((Validator<Object>) validator);
                        }
                    }

                    builder.bind(
                            entity1 -> reflectionService.getValue(entity1, fieldName),
                            (entity1, o) -> reflectionService.setValue(entity1, fieldName, o)
                    );
                }

                // Apply RBAC field-level permissions AFTER binding (binding can reset readonly status)
                if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY) {
                    if (component instanceof HasValue) {
                        ((HasValue<?, ?>) component).setReadOnly(true);
                    }
                }

                if (component instanceof HasSize) {
                    ((HasSize) component).setWidthFull();
                }
                if (component instanceof HasLabel) {
                    ((HasLabel) component).setLabel(component.getTranslation(element.label()));
                    form.add(component);
                    form.setColspan(component, element.span());
                } else {
                    FormLayout.FormItem formItem = form.addFormItem(component, component.getTranslation(element.label()));
                    form.setColspan(formItem, element.span());
                }
            } else {
                Component collection = element.factory().createCollection(
                        reflectionService.getId(entity),
                        routeRenderer,
                        element,
                        context
                );
                form.add(collection);
                form.setColspan(collection, element.span());
            }
        }
    }
}
