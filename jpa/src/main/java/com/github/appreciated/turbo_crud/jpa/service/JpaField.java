package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.config.model.Validation;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;

import java.util.List;

public class JpaField extends Field<String, String> {


    public JpaField(Class<? extends TurboCrudFieldFactory> factory) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory);
    }

    public JpaField(Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory, primary);
    }

    public JpaField(Class<? extends TurboCrudFieldFactory> factory, String values) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory, values);
    }

    public JpaField(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory, primary, required);
    }

    public JpaField(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory, primary, required, validation);
    }

    public JpaField(Class<? extends TurboCrudFieldFactory> factory, String field, String filterField, String dataStore, List<String> children) {
        super((Class<? extends TurboCrudFieldFactory<String, String>>) factory, field, filterField, dataStore, children);
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
        return new Field.Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory, primary));
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory, String values) {
        return new Field.Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory, values));
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Field.Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory, primary, required));
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Field.Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory, primary, required, validation));
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory, String field, String filterField, String dataStore, List<String> children) {
        return new Field.Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory, field, filterField, dataStore, children));
    }

    public static Builder<String, String> of(Class<? extends TurboCrudFieldFactory> factory) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<String, String>>)factory));
    }
}

