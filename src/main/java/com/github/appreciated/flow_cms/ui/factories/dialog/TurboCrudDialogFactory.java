package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import com.vaadin.flow.component.dialog.Dialog;

public interface TurboCrudDialogFactory {
    Dialog createDialog(String entityId, String foreignKeyValue, CollectionFactoryConfig factoryConfig, DetailFactory detailFactory, TurboCrudDetailFactoryRegistry detailFactoryRegistry, OnStoreListener listener, FormCreator formCreator);
}
