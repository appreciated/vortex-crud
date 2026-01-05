package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
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
public class Collection<ModelClass, FieldType, RepositoryType> implements InternalFormElement<FieldType>, CollectionConfiguration<ModelClass, FieldType, RepositoryType> {

    @lombok.NonNull
    private FieldType field;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    @I18nKey
    @lombok.NonNull
    private String label;

    private int span;

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

    /**
     * Custom builder that injects the dataStoreConfig into the child form after building.
     */
    public static class CollectionBuilder<ModelClass, FieldType, RepositoryType> {
        public Collection<ModelClass, FieldType, RepositoryType> build() {
            Collection<ModelClass, FieldType, RepositoryType> instance = new Collection<>(
                    field, readOnly, readOnlyForRoles, label, span,
                    listFactory, dialogFactory, emptyMessage, form, titleField,
                    dataStoreConfig, oneToMany, manyToMany, children
            );

            // Inject collection's dataStoreConfig into child form
            if (instance.form != null && instance.dataStoreConfig != null) {
                instance.form.dataStoreConfig(instance.dataStoreConfig);
                if (instance.form.title() == null) {
                    instance.form.title(instance.emptyMessage);
                }
            }

            return instance;
        }
    }

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig.dataStoreInstance();
    }
}
