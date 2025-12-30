package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.config.visitor.I18nConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.config.visitor.I18nVisitable;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DataStoreConfig<ModelClass, FieldType, RepositoryType> implements HasDataStore<FieldType, ModelClass>, I18nVisitable {

    private RepositoryType factory;
    private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;

    private Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields;

    @Builder.Default
    private DataStoreHooks<?> hooks = new DataStoreHooks<>();

    @Override
    public void accept(I18nConfigurationVisitor visitor) {
        visitor.visit(this);
        if (fields != null) {
            fields.values().forEach(field -> field.accept(visitor));
        }
    }
}
