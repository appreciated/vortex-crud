package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import com.vaadin.flow.component.dialog.Dialog;

public interface FlowCmsDialogFactory {
    Dialog createDialog(String id, CollectionFactoryConfig factoryConfig, DetailFactory detailFactory, FlowCmsDetailFactoryRegistry detailFactoryRegistry, OnStoreListener listener, FormCreator formCreator);
}
