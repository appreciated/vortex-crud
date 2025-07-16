package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;

import java.util.List;

public class JooqField extends Field<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {


    public JooqField(Class<? extends VortexCrudFieldFactory> factory) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory);
    }

    public JooqField(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary);
    }

    public JooqField(Class<? extends VortexCrudFieldFactory> factory, String values) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, values);
    }

    public JooqField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary, required);
    }

    public JooqField(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary, required, validation);
    }

    public JooqField(Class<? extends VortexCrudFieldFactory> factory, TableField<?, ?> field, TableField<?, ?> filterField, Class<? extends TableRecord<?>> dataStore, List<TableField<?, ?>> children) {
        super((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, field, filterField, dataStore, children);
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary));
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory, String values) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, values));
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary, required));
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, primary, required, validation));
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory, TableField<?, ?> field, TableField<?, ?> filterField, Class<TableRecord<?>> dataStore, List<TableField<?, ?>> children) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory, field, filterField, dataStore, children));
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(Class<? extends VortexCrudFieldFactory> factory) {
        return new Builder<>(new Field<>((Class<? extends VortexCrudFieldFactory<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>>>) factory));
    }
}

