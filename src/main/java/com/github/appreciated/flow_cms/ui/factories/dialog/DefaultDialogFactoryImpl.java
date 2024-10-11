package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.vaadin.flow.component.dialog.Dialog;

public class DefaultDialogFactoryImpl implements FlowCmsDialogFactory {

    private final FlowCmsEntityManagerService entityManagerService;

    public DefaultDialogFactoryImpl(FlowCmsEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
    }

    @Override
    public Dialog createDialog(String id, CollectionFactoryConfig factoryConfig) {
        return new Dialog();
    }
}