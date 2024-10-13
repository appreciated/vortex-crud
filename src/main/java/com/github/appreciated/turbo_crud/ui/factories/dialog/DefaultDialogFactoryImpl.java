package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.CollectionFactoryConfig;
import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.TableConfig;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class DefaultDialogFactoryImpl implements TurboCrudDialogFactory {

    private final TurboCrudConfigService configService;
    private final TurboCrudEntityManagerService entityManagerService;

    public DefaultDialogFactoryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerService entityManagerService) {
        this.configService = configService;
        this.entityManagerService = entityManagerService;
    }

    @Override
    public Dialog createDialog(String entityId,
                               String foreignKeyValue,
                               CollectionFactoryConfig factoryConfig,
                               DetailFactory detailFactory,
                               TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                               OnStoreListener listener,
                               FormCreator formCreator) {
        String table = factoryConfig.getTable();
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(dialog.getTranslation(factoryConfig.getLabel()));

        GenericEntity recordById = entityManagerService.getRecordById(table, entityId);
        if (recordById == null) {
            recordById = new GenericEntity();
        }

        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        binder.setBean(recordById);
        createFooter(table, foreignKeyValue, factoryConfig, binder, recordById, dialog, listener);
        FormLayout layout = new FormLayout();

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(table);
        formCreator.bindAndAddToLayout(table, detailFactory, recordById, detailFactoryRegistry, tables, binder, layout, formCreator);

        dialog.add(layout);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(350, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(String table, String foreignKeyValue, CollectionFactoryConfig factoryConfig, Binder<GenericEntity> binder, GenericEntity entity, Dialog dialog, OnStoreListener listener) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entity.put(factoryConfig.getForeignKeyColumn(), foreignKeyValue);
                if (EntityUtil.isNew(entity)) {
                    entityManagerService.insertRecord(table, entity);
                } else {
                    entityManagerService.updateRecordById(table, EntityUtil.getId(entity), entity);
                }
                binder.setBean(entity);
                dialog.close();
                listener.onStore();
            } catch (ValidationException e) {
                Notification notification = Notification.show(cancelButton.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
    }
}