package com.github.appreciated.vortex_crud.core.config.model;

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
public class Collection<ModelClass, FieldType, RepositoryType> implements CollectionConfiguration<ModelClass, FieldType, RepositoryType> {

    private String label;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> factory;

    private String emptyMessage;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private FieldType titleField;

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;

    public String label() {
        return label;
    }

    public VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> factory() {
        return factory;
    }

    public String emptyMessage() {
        return emptyMessage;
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> child() {
        return child;
    }

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig != null ? dataStoreConfig.dataStoreInstance() : null;
    }

    public OneToMany<ModelClass, FieldType, RepositoryType> oneToMany() {
        return oneToMany;
    }

    public ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany() {
        return manyToMany;
    }

    public List<FieldType> children() {
        return children;
    }
}
