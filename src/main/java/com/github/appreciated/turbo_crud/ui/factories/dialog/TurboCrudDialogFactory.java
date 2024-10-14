package com.github.appreciated.turbo_crud.ui.factories.dialog;


import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.DialogConfig;
import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.vaadin.flow.component.dialog.Dialog;

public interface TurboCrudDialogFactory {
    Dialog createDialog(String entityId, String foreignKeyValue, FormElement formElement, DetailFactory detailFactory, TurboCrudDetailFactoryRegistry detailFactoryRegistry, OnStoreListener listener, FormCreator formCreator);
}
