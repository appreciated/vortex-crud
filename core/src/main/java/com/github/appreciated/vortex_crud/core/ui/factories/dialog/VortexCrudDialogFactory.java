package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {
    Dialog create(@Nullable Object entityId,
                  @Nullable Object foreignKeyValue,
                  @Nullable FieldType foreignKeyField,
                  RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                  CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                  RepositoryType dataStoreKey,
                  VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
                  OnStoreListener storeListener,
                  OnCancelListener cancelListener,
                  FormCreator<ModelClass, FieldType, RepositoryType> formCreator);
}
