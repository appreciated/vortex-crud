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
    private List<Validator<?>> validators;
    private boolean required;
    private List<String> writeRoles;
    private List<String> readOnlyRoles;
    @Builder.Default
    private VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new MultiSelectFieldFactory<>();
    FieldType field;
    FieldType searchField;
    List<FieldType> children;
    VortexCrudDataStore<FieldType, ?> dataStore;
}
