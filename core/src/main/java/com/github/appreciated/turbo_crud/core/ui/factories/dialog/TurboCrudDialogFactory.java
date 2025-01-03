package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.CollectionData;
import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface TurboCrudDialogFactory<DataStoreId, FieldId> {
    Dialog create(@Nullable String entityId,
                      @Nullable String foreignKeyValue,
                      @Nullable FieldId foreignKeyField,
                      RouteRenderer<DataStoreId, FieldId> formRouteRenderer,
                      CollectionData<DataStoreId, FieldId> config,
                      DataStoreId dataStore,
                      TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                      OnStoreListener listener,
                      FormCreator<DataStoreId, FieldId> formCreator);
}
