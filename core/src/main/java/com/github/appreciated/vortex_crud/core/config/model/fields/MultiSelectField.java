package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.MultiSelectFieldFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultiSelectField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    RepositoryType dataStore;
    FieldType field;
    FieldType filterField;
    List<FieldType> children;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @Builder.Default
    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new MultiSelectFieldFactory<>();

    @Override
    public VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factoryInstance() {
        return factory;
    }

    @Override
    public List<Validator<?>> validators() {
        return null;
    }
}
