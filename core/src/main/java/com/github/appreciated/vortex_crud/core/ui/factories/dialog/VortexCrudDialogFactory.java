package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface VortexCrudDialogFactory<DataStoreId, FieldId, KeyType> {
    Dialog create(@Nullable String entityId,
                  @Nullable Object foreignKeyValue,
                  @Nullable FieldId foreignKeyField,
                  RouteRenderer<DataStoreId, FieldId, KeyType> formRouteRenderer,
                  CollectionConfiguration<DataStoreId, FieldId, KeyType> config,
                  KeyType dataStoreKey,
                  VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                  OnStoreListener storeListener,
                  OnCancelListener cancelListener,
                  FormCreator<DataStoreId, FieldId, KeyType> formCreator);
}
