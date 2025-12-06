package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {
    Dialog create(@Nullable Object entityId,
                  @Nullable Object foreignKeyValue,
                  @Nullable FieldType foreignKeyField,
                  RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                  CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                  VortexCrudDataStore<FieldType, ModelClass> dataStore,
                  VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                  OnStoreListener storeListener,
                  OnCancelListener cancelListener);
}
