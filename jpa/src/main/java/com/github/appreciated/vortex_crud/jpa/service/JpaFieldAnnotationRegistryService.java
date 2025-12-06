package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Registry for JPA field annotations used by Vortex CRUD.
 * <p>
 * Keeps a mapping between JPA annotation types (e.g. {@link TextField}) and
 * the corresponding {@link VortexCrudFieldFactory} implementation that should
 * be used to render/configure that field.
 * <p>
 * Additionally, exposes the complete set of supported annotations so other
 * services can quickly check for supported annotations on entity fields.
 */
@Service
public class JpaFieldAnnotationRegistryService {

    private final Set<Class<? extends Annotation>> registeredAnnotations = new LinkedHashSet<>();

    public JpaFieldAnnotationRegistryService() {
        // Register mapping annotations -> factories
        register(BigDecimalNumberField.class);
        register(CheckboxField.class);
        register(DateField.class);
        register(DateTimePickerField.class);
        register(DoubleNumberField.class);
        register(IdField.class);
        register(ImageField.class);
        register(IntegerNumberField.class);
        register(ReferenceField.class);
        register(MultiSelectField.class);
        register(SelectField.class);
        register(MultiSelectValueField.class);
        register(TextAreaField.class);
        register(TextField.class);
        register(EmailField.class);
        register(VideoField.class);
        register(PasswordField.class);
        register(PdfField.class);
        register(FileField.class);
    }

    private void register(Class<? extends Annotation> annotation) {
        registeredAnnotations.add(annotation);
    }

    public boolean hasFieldAnnotation(Field field) {
        return registeredAnnotations.stream().anyMatch(field::isAnnotationPresent);
    }
}
