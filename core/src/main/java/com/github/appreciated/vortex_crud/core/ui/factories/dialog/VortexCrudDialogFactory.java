package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass> {
    Dialog create(@Nullable String entityId,
                      @Nullable String foreignKeyValue,
                      @Nullable FieldId foreignKeyField,
                      RouteRenderer<DataStoreId, FieldId, ModelClass> formRouteRenderer,
                      CollectionConfiguration<DataStoreId, FieldId, ModelClass> config,
                      DataStoreId dataStore,
                      VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory,
                      OnStoreListener listener,
                      FormCreator<DataStoreId, FieldId, ModelClass> formCreator);
}
