package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;

/**
 * Base implementation for dialog factories that render CRUD forms.
 * Stateless factory: dependencies are retrieved from the context.
 */
public abstract class AbstractFormDialogFactory<ModelClass, FieldType, RepositoryType>
        implements VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Dialog create(@Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                         VortexCrudDataStore<FieldType, ModelClass> dataStore,
                         VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener) {

        VortexCrudDataStoreUtilStrategy dataStoreUtil = context.dataStoreUtil();
        FormCreator<ModelClass, FieldType, RepositoryType> formCreator = context.formCreator();

        Dialog dialog = instantiateDialog();

        ModelClass recordById = dataStore.getRecordById(entityId);
        if (recordById == null) {
            recordById = dataStore.newInstance();
        }

        if (dataStoreUtil.isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<Object> binder = new Binder<>(Object.class);
        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener, context, dataStore);
        FormLayout layout = new FormLayout();

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = formRouteRenderer.dataStoreConfig();

        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = formRouteRenderer.configuration();
        formCreator.bindAndAddToLayout(tables.factory(), formRouteRenderer, configuration.children(), recordById,
                context, tables, binder, layout);

        dialog.add(layout);
        return dialog;
    }

    protected abstract Dialog instantiateDialog();

    private void createFooter(Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, ModelClass entity, Dialog dialog,
                              OnStoreListener listener, OnCancelListener onCancelListener,
                              VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                              VortexCrudDataStore<FieldType, ModelClass> dataStore) {

        VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy = context.foreignKeyResolutionStrategy();
        VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver = context.fieldNameResolver();
        VortexCrudDataStoreUtilStrategy dataStoreUtil = context.dataStoreUtil();

        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> {
            onCancelListener.onCancel();
            dialog.close();
        });
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                // Cast dataStore to handle object entity in foreignKeyStrategy?
                foreignKeyResolutionStrategy.resolveForeignKey(entity, foreignKeyField, foreignKeyValue, (VortexCrudDataStore<FieldType, Object>)dataStore, fieldNameResolver);

                if (dataStoreUtil.isNew(entity)) {
                    dataStore.insertRecord(entity);
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
