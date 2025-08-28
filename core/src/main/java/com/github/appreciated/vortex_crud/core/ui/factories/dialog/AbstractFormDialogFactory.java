package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

/**
 * Base implementation for dialog factories that render CRUD forms.
 */
public abstract class AbstractFormDialogFactory<DataStoreId, FieldId, KeyType>
        implements VortexCrudDialogFactory<DataStoreId, FieldId, KeyType> {

    protected final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    protected final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    protected final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    protected final VortexCrudForeignKeyResolutionStrategy<FieldId> foreignKeyResolutionStrategy;
    protected final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    protected VortexCrudDataStore<FieldId, Object> dataStore;

    public AbstractFormDialogFactory(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                                     VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                                     VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                                     VortexCrudForeignKeyResolutionStrategy<FieldId> foreignKeyResolutionStrategy,
                                     VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.configService = configService;
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.foreignKeyResolutionStrategy = foreignKeyResolutionStrategy;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Dialog create(@org.jetbrains.annotations.Nullable Object entityId,
                         @org.jetbrains.annotations.Nullable Object foreignKeyValue,
                         @org.jetbrains.annotations.Nullable FieldId foreignKeyField,
                         RouteRenderer<DataStoreId, FieldId, KeyType> formRouteRenderer,
                         CollectionConfiguration<DataStoreId, FieldId, KeyType> config,
                         KeyType dataStoreKey,
                         VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener,
                         FormCreator<DataStoreId, FieldId, KeyType> formCreator) {

        this.dataStore = (VortexCrudDataStore<FieldId, Object>) dataStoreFactoryRegistry.getDataStore(dataStoreKey);
        Dialog dialog = instantiateDialog();

        Object recordById = this.dataStore.getRecordById(entityId);
        if (recordById == null) {
            recordById = this.dataStore.newInstance();
        }

        if (dataStoreUtil.isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<Object> binder = new Binder<>(Object.class);
        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener);
        FormLayout layout = new FormLayout();

        DataStoreConfig<DataStoreId, FieldId, KeyType> tables = configService.getConfiguration().getDataStores().get(dataStoreKey);

        formCreator.bindAndAddToLayout(dataStoreKey, formRouteRenderer, formRouteRenderer.getConfiguration(), recordById,
                routeFactory, tables, binder, layout, formCreator);

        dialog.add(layout);
        return dialog;
    }

    protected abstract Dialog instantiateDialog();

    private void createFooter(Object foreignKeyValue, FieldId foreignKeyField, Binder<Object> binder, Object entity, Dialog dialog,
                               OnStoreListener listener, OnCancelListener onCancelListener) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> {
            onCancelListener.onCancel();
            dialog.close();
        });
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                foreignKeyResolutionStrategy.resolveForeignKey(entity, foreignKeyField, foreignKeyValue, dataStore, fieldNameResolver);
                if (dataStoreUtil.isNew(entity)) {
                    if (dataStore.getModelClass().isInstance(entity)) {
                        dataStore.insertRecord(entity);
                    }
                } else {
                    dataStore.updateRecordById(entity);
                }
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
