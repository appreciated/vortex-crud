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

        for (InternalFormElement<ModelClass, FieldType, RepositoryType> element : fieldsViewConfig) {
            if (element.type() == ViewFieldType.COLLECTION) {
                addCollectionToForm(element, entity, routeRenderer, context, form);
            } else {
                processField(element, dataStoreKey, routeRenderer, fieldsConfig, entity, context, binder, form);
            }
        }
    }

    private void addCollectionToForm(InternalFormElement<ModelClass, FieldType, RepositoryType> element,
                                     Object entity,
                                     RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                     VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                     FormLayout form) {
        ReflectionService<FieldType> reflectionService = context.reflectionService();
        Component collection = element.factory().createCollection(
                reflectionService.getId(entity),
                routeRenderer,
                element,
                context
        );
        form.add(collection);
        form.setColspan(collection, element.span());
    }

    private void processField(InternalFormElement<ModelClass, FieldType, RepositoryType> element,
                              RepositoryType dataStoreKey,
                              RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                              Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig,
                              Object entity,
                              VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                              Binder<Object> binder,
                              FormLayout form) {
        FieldType fieldName = element.field();
        Field<ModelClass, FieldType, RepositoryType> field = fieldsConfig.get(fieldName);
        if (field == null) {
            throw new IllegalStateException("Field '" + fieldName + "' not found in the config under table '" + dataStoreKey + "'");
        }

        VortexCrudRbacPermissionChecker.FieldAccessLevel userFieldAccess = getFieldAccessLevel(routeRenderer, field);
        if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE) {
            return;
        }

        VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = field.factory();
        Component component = factory.createComponent(dataStoreKey, fieldName, field, context);

        bindField(component, fieldName, field, entity, context, binder);

        applyFieldAccessLevel(component, userFieldAccess);
        configureComponentSize(component);
        addComponentToForm(component, element, form);
    }

    private VortexCrudRbacPermissionChecker.FieldAccessLevel getFieldAccessLevel(RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                                                                 Field<ModelClass, FieldType, RepositoryType> field) {
        if (permissionChecker != null) {
            return permissionChecker.getUserFieldAccess(routeRenderer, field);
        }
        return null;
    }

    private void bindField(Component component,
                           FieldType fieldName,
                           Field<ModelClass, FieldType, RepositoryType> field,
                           Object entity,
                           VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                           Binder<Object> binder) {
        if (component instanceof NumericIdTextField) {
            bindNumericIdTextField((NumericIdTextField) component, fieldName, context, binder);
        } else {
            bindStandardField(component, fieldName, field, entity, context, binder);
        }
    }

    private void bindNumericIdTextField(NumericIdTextField component,
                                        FieldType fieldName,
                                        VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                        Binder<Object> binder) {
        ReflectionService<FieldType> reflectionService = context.reflectionService();
        Binder.BindingBuilder<Object, String> builder = binder.forField(component);

        Converter<String, Object> numericConverter = new Converter<String, Object>() {
            @Override
            public Result<Object> convertToModel(String value, com.vaadin.flow.data.binder.ValueContext context) {
                return Result.ok(value);
            }

            @Override
            public String convertToPresentation(Object value, com.vaadin.flow.data.binder.ValueContext context) {
                return value == null ? "" : value.toString();
            }
        };

        builder.withConverter(numericConverter).bind(
                entity1 -> reflectionService.getValue(entity1, fieldName),
                (entity1, o) -> {} // Read-only
        );
    }

    private void bindStandardField(Component component,
                                   FieldType fieldName,
                                   Field<ModelClass, FieldType, RepositoryType> field,
                                   Object entity,
                                   VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                   Binder<Object> binder) {
        ReflectionService<FieldType> reflectionService = context.reflectionService();
        Binder.BindingBuilder<Object, Object> builder = (Binder.BindingBuilder<Object, Object>) binder.forField((HasValue<?, ?>) component);

        if (fieldName instanceof String propertyName) {
            builder = builder.withValidator(new BeanValidator(entity.getClass(), propertyName));
        }
        if (field.required() && component instanceof HasValue) {
            builder = builder.asRequired();
        }

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

    private void applyFieldAccessLevel(Component component, VortexCrudRbacPermissionChecker.FieldAccessLevel userFieldAccess) {
        if (userFieldAccess == VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY) {
            if (component instanceof HasValue) {
                ((HasValue<?, ?>) component).setReadOnly(true);
            }
        }
    }

    private void configureComponentSize(Component component) {
        if (component instanceof HasSize) {
            ((HasSize) component).setWidthFull();
        }
    }

    private void addComponentToForm(Component component,
                                    InternalFormElement<ModelClass, FieldType, RepositoryType> element,
                                    FormLayout form) {
        String translation = component.getTranslation(element.label());
        if (component instanceof HasLabel) {
            ((HasLabel) component).setLabel(translation);
            form.add(component);
            form.setColspan(component, element.span());
        } else {
            FormLayout.FormItem formItem = form.addFormItem(component, translation);
            form.setColspan(formItem, element.span());
        }
    }
}
