package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.CollectionData;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface TurboCrudDialogFactory {
    <T> Dialog create(@Nullable String entityId,
                  @Nullable String foreignKeyValue,
                  @Nullable String foreignKeyField,
                  Route<T> formRoute,
                  CollectionData config,
                  T dataStore,
                  TurboCrudRouteFactoryRegistry routeFactory,
                  OnStoreListener listener,
                  FormCreator formCreator);
}
