package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Thin Field type for ReferenceFieldFactory.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReferenceField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    List<Validator<?>> validators;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @Builder.Default
    private VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new ReferenceFieldFactory<>();

    @lombok.NonNull
    private FieldType field;
    @lombok.NonNull
    private FieldType searchField;
    private List<FieldType> children;
    private VortexCrudDataStore<FieldType, ?> dataStore;
}
