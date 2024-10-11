package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.vaadin.flow.component.dialog.Dialog;

public interface FlowCmsDialogFactory {
    Dialog createDialog(String id, CollectionFactoryConfig factoryConfig);
}
