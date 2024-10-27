package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nullable;

public interface TurboCrudDialogFactory {
    Dialog createDialog(@Nullable String entityId,
                        @Nullable String foreignKeyValue,
                        @Nullable String foreignKeyField,
                        Route formRoute,
                        String repository,
                        TurboCrudRouteFactoryRegistry routeFactory,
                        OnStoreListener listener,
                        FormCreator formCreator);
}
