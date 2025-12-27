package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.components.NumericIdTextField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.validator.BeanValidator;
import org.springframework.stereotype.Service;

@Service
public class DataBinder<ModelClass, FieldType, RepositoryType> {

    @SuppressWarnings("unchecked")
    public void bindComponent(Component component,
                              FieldType fieldName,
                              Field<ModelClass, FieldType, RepositoryType> field,
                              Object entity,
                              Binder<Object> binder,
                              ReflectionService<FieldType> reflectionService) {
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
                    (entity1, o) -> {
                    } // Read-only, no need to set
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
    }
}
