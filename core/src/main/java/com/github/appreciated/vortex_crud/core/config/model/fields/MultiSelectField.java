package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.MultiSelectFieldFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Thin Field type for MultiSelectFieldFactory.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultiSelectField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    List<Validator<?>> validators;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @Builder.Default
    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new MultiSelectFieldFactory<>();
    FieldType field;
    FieldType filterField;
    List<FieldType> children;
    VortexCrudDataStore<FieldType, ?> dataStore;

    public FieldType field() {
        return field;
    }

    public FieldType filterField() {
        return filterField;
    }

    public List<FieldType> children() {
        return children;
    }

    public VortexCrudDataStore<FieldType, ?> dataStore() {
        return dataStore;
    }
}
