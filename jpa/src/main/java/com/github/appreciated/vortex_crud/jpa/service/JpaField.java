package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public class JpaField extends Field<JpaRepository<?, ?>, String> {

    public JpaField(Class<? extends VortexCrudFieldFactory> factory) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory, primary);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, String values) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory, values);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory, primary, required);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory, primary, required, validation);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, String field, String filterField, JpaRepository<?, ?> dataStore, List<String> children) {
        super((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>) factory, field, filterField, dataStore, children);
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory, primary));
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory, String values) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory, values));
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory, primary, required));
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory, primary, required, validation));
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory, String field, String filterField, JpaRepository<?, ?> dataStore, List<String> children) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory, field, filterField, dataStore, children));
    }

    public static Builder<JpaRepository<?, ?>, String> of(Class<? extends VortexCrudFieldFactory> factory) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String>>)factory));
    }
}
