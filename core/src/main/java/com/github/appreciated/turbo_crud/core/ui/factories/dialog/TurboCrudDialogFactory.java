package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.CollectionData;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface TurboCrudDialogFactory<DataStoreId, FieldId> {
    Dialog create(@Nullable String entityId,
                      @Nullable String foreignKeyValue,
                      @Nullable String foreignKeyField,
                      Route<DataStoreId, FieldId> formRoute,
                      CollectionData<DataStoreId> config,
                      DataStoreId dataStore,
                      TurboCrudRouteFactoryRegistry routeFactory,
                      OnStoreListener listener,
                      FormCreator formCreator);
}
