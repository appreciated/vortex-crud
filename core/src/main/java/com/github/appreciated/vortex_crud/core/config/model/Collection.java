package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.config.visitor.ConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.config.visitor.Visitable;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
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
public class Collection<ModelClass, FieldType, RepositoryType> implements CollectionConfiguration<ModelClass, FieldType, RepositoryType>, Visitable {

    @I18nKey
    private String label;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> factory;

    @I18nKey
    private String emptyMessage;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> form;

    private FieldType titleField;

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig != null ? dataStoreConfig.dataStoreInstance() : null;
    }

    @Override
    public void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
        if (form != null) {
            form.accept(visitor);
        }
        if (dataStoreConfig != null) {
            dataStoreConfig.accept(visitor);
        }
        // OneToMany and ManyToMany might need visiting if they have configuration
    }
}
