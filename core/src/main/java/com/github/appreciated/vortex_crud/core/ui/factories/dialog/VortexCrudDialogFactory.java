package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface VortexCrudDialogFactory<DataStoreId, FieldId> {
    Dialog create(@Nullable String entityId,
                  @Nullable String foreignKeyValue,
                  @Nullable FieldId foreignKeyField,
                  RouteRenderer<DataStoreId, FieldId> formRouteRenderer,
                  CollectionConfiguration<DataStoreId, FieldId> config,
                  Class<? extends DataStoreId> dataStoreKey,
                  VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                  OnStoreListener listener,
                  FormCreator<DataStoreId, FieldId> formCreator);
}
