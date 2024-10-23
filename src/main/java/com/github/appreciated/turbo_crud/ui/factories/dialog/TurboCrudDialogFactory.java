package com.github.appreciated.turbo_crud.ui.factories.dialog;


import com.github.appreciated.turbo_crud.config.model.FormItem;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;

public interface TurboCrudDialogFactory {
    Dialog createDialog(String entityId,
                        String foreignKeyValue,
                        Route route,
                        FormItem formItem,
                        TurboCrudRouteFactoryRegistry childFactory,
                        OnStoreListener listener,
                        FormCreator formCreator);
}
