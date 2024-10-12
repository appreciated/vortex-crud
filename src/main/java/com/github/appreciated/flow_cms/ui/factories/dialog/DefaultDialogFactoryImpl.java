package com.github.appreciated.flow_cms.ui.factories.dialog;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactory;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

public class DefaultDialogFactoryImpl implements FlowCmsDialogFactory {

    private final FlowCmsEntityManagerService entityManagerService;

    public DefaultDialogFactoryImpl(FlowCmsEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
    }

    @Override
    public Dialog createDialog(String id, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry registry) {
        String table = factoryConfig.getTable();
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(dialog.getTranslation(factoryConfig.getLabel()));
        createFooter(dialog);
        FlowCmsDetailFactory form = registry.getFactory("form");
        Component component = form.renderDetail(table,
                null,
                factoryConfig.getDetailFactory(),
                entityManagerService.getRecordById(table, id),
                true,
                true,
                registry
        );
        dialog.add(component);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(350, Unit.PIXELS);
        return dialog;
    }

    private static void createFooter(Dialog dialog) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), e -> dialog.close());
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }
}