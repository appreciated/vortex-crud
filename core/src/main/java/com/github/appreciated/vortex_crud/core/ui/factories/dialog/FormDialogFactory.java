package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
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

public class FormDialogFactory <DataStoreId, FieldId> implements VortexCrudDialogFactory<DataStoreId, FieldId> {

    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private VortexCrudDataStore<FieldId> dataStore;

    public FormDialogFactory(VortexCrudConfigService<DataStoreId, FieldId> configService, VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.configService = configService;
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                             @Nullable String foreignKeyValue,
                             @Nullable FieldId foreignKeyField,
                             RouteRenderer<DataStoreId, FieldId> formRouteRenderer,
                             CollectionConfiguration<DataStoreId, FieldId> config,
                             DataStoreId dataStore,
                             VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                             OnStoreListener listener,
                             FormCreator<DataStoreId, FieldId> formCreator) {

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

        formCreator.bindAndAddToLayout(dataStore, formRouteRenderer, formRouteRenderer.getConfiguration(), recordById, routeFactory, tables, binder, layout, formCreator);

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
                    entity.put(fieldNameResolver.getKeyForFieldId(foreignKeyField), foreignKeyValue);
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