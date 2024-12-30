package com.github.appreciated.turbo_crud.core.ui.factories.dialog;

import com.github.appreciated.turbo_crud.core.config.model.CollectionData;
import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;

public class FormDialogFactory <DataStoreId, FieldId> implements TurboCrudDialogFactory<DataStoreId, FieldId> {

    private final TurboCrudConfigService <DataStoreId, FieldId> configService;
    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private TurboCrudDataStore<FieldId> dataStore;

    public FormDialogFactory(TurboCrudConfigService<DataStoreId, FieldId> configService, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry) {
        this.configService = configService;
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                             @Nullable String foreignKeyValue,
                             @Nullable FieldId foreignKeyField,
                             Route<DataStoreId, FieldId> formRoute,
                             CollectionData<DataStoreId, FieldId> config,
                             DataStoreId dataStore,
                             TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                             OnStoreListener listener,
                             FormCreator formCreator) {

        this.dataStore = dataStoreFactoryRegistry.getFactory(dataStore);
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");

        GenericEntity recordById = this.dataStore.getRecordById(entityId);
        if (recordById == null) {
            recordById = new GenericEntity();
        }

        if (DataStoreUtil.isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, listener);
        FormLayout layout = new FormLayout();

        DataStoreConfig<DataStoreId, FieldId> tables = configService.getConfiguration().getDataStores().get(dataStore);

        formCreator.bindAndAddToLayout(dataStore, formRoute, formRoute.getConfiguration(), recordById, routeFactory, tables, binder, layout, formCreator);

        dialog.add(layout);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(String foreignKeyValue, FieldId foreignKeyField, Binder<GenericEntity> binder, GenericEntity entity, Dialog dialog, OnStoreListener listener) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                if (foreignKeyField != null && foreignKeyValue != null) {
                    entity.put(foreignKeyField, foreignKeyValue);
                }
                if (DataStoreUtil.isNew(entity)) {
                    dataStore.insertRecord(entity);
                } else {
                    dataStore.updateRecordById(DataStoreUtil.getId(entity), entity);
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