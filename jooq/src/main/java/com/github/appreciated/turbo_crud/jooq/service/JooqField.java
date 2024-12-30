package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.config.model.Validation;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.List;

public class JooqField extends Field<Table<?>, TableField<?,?>> {


    public JooqField(Class<? extends TurboCrudFieldFactory> factory) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory);
    }

    public JooqField(Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory, primary);
    }

    public JooqField(Class<? extends TurboCrudFieldFactory> factory, String values) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory, values);
    }

    public JooqField(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory, primary, required);
    }

    public JooqField(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory, primary, required, validation);
    }

    public JooqField(Class<? extends TurboCrudFieldFactory> factory, TableField field, TableField filterField, Table<?> dataStore, List<String> children) {
        super((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>) factory, field, filterField, dataStore, children);
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory, primary));
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory, String values) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory, values));
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory, primary, required));
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory, primary, required, validation));
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory, TableField field, TableField filterField, Table<?> dataStore, List<String> children) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory, field, filterField, dataStore, children));
    }

    public static Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudFieldFactory> factory) {
        return new Builder<>(new Field<>((Class<? extends TurboCrudFieldFactory<Table<?>, TableField<?,?>>>)factory));
    }
}

