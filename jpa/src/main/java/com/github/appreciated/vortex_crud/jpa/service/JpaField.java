package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class JpaField extends Field<CrudRepository<?,?>, String> {


    public JpaField(Class<? extends VortexCrudFieldFactory> factory) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory, primary);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, String values) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory, values);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory, primary, required);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory, primary, required, validation);
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, String field, String filterField, CrudRepository<?,?> dataStore, List<String> children) {
        super((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>) factory, field, filterField, dataStore, children);
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory, primary));
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory, String values) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory, values));
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory, primary, required));
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory, primary, required, validation));
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory, String field, String filterField, CrudRepository<?,?> dataStore, List<String> children) {
        return new Field.Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory, field, filterField, dataStore, children));
    }

    public static Builder<CrudRepository<?,?>, String> of(Class<? extends VortexCrudFieldFactory> factory) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<CrudRepository<?,?>, String>>)factory));
    }
}

