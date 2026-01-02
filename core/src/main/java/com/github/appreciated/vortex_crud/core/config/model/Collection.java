package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class Collection<ModelClass, FieldType, RepositoryType> extends InternalFormElement<ModelClass, FieldType, RepositoryType> implements CollectionConfiguration<ModelClass, FieldType, RepositoryType>, ValidatableConfiguration {

    private ListCollectionFactory<ModelClass, FieldType, RepositoryType> listFactory;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory;

    @I18nKey
    private String emptyMessage;

    private FormRoute<ModelClass, FieldType, RepositoryType> form;

    private FieldType titleField;

    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig.dataStoreInstance();
    }
}
