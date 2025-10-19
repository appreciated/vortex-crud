package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Registry for JPA field annotations used by Vortex CRUD.
 * <p>
 * Keeps a mapping between JPA annotation types (e.g. {@link TextField}) and
 * the corresponding {@link VortexCrudFieldFactory} implementation that should
 * be used to render/configure that field.
 * <p>
 * Additionally exposes the complete set of supported annotations so other
 * services can quickly check for supported annotations on entity fields.
 */
@Service
public class JpaFieldAnnoationRegistryService {

    private final Map<Class<? extends Annotation>, Class<? extends VortexCrudFieldFactory>> annotationToFactory = new HashMap<>();
    private final Set<Class<? extends Annotation>> registeredAnnotations = new LinkedHashSet<>();

    public JpaFieldAnnoationRegistryService() {
        // Register mapping annotations -> factories
        register(BigDecimalNumberField.class, BigDecimalNumberFieldFactory.class);
        register(CheckboxField.class, CheckboxFieldFactory.class);
        register(DateField.class, DateFieldFactory.class);
        register(DateTimePickerField.class, DateTimePickerFactory.class);
        register(DoubleNumberField.class, DoubleNumberFieldFactory.class);
        register(IdField.class, IdFieldFactory.class);
        register(ImageField.class, ImageFieldFactory.class);
        register(IntegerNumberField.class, IntegerNumberFieldFactory.class);
        register(ReferenceField.class, ReferenceFieldFactory.class);
        register(SelectField.class, SelectFieldFactory.class);
        register(TextAreaField.class, TextAreaFieldFactory.class);
        register(TextField.class, TextFieldFactory.class);
        register(VideoField.class, VideoFieldFactory.class);
    }

    private void register(Class<? extends Annotation> annotation,
                          Class<? extends VortexCrudFieldFactory> factory) {
        annotationToFactory.put(annotation, factory);
        registeredAnnotations.add(annotation);
    }

    private void trackOnly(Class<? extends Annotation> annotation) {
        registeredAnnotations.add(annotation);
    }

    public boolean hasFieldAnnotation(Field field) {
        return registeredAnnotations.stream().anyMatch(field::isAnnotationPresent);
    }
}
