package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
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
 */
public abstract class AbstractFormDialogFactory<ModelClass, FieldType, RepositoryType>
        implements VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Dialog create(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                         @Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                         VortexCrudDataStore<FieldType, ModelClass> dataStore,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener) {

        VortexCrudDataStore<FieldType, Object> objectDataStore = (VortexCrudDataStore<FieldType, Object>) dataStore;
        Dialog dialog = instantiateDialog();

        Object recordById = objectDataStore.getRecordById(entityId);
        if (recordById == null) {
            recordById = objectDataStore.newInstance();
        }

        if (context.getDataStoreUtil().isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<Object> binder = new Binder<>(Object.class);
        binder.setBean(recordById);
        createFooter(context, foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener, objectDataStore);
        FormLayout layout = new FormLayout();

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = formRouteRenderer.dataStoreConfig();

        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = formRouteRenderer.configuration();
        context.getFormCreator().bindAndAddToLayout(context, tables.factory(), formRouteRenderer, configuration.children(), recordById,
                tables, binder, layout);

        dialog.add(layout);
        return dialog;
    }

    protected abstract Dialog instantiateDialog();

    private void createFooter(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, Object entity, Dialog dialog,
                              OnStoreListener listener, OnCancelListener onCancelListener, VortexCrudDataStore<FieldType, Object> dataStore) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> {
            onCancelListener.onCancel();
            dialog.close();
        });
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                context.getForeignKeyResolutionStrategy().resolveForeignKey(entity, foreignKeyField, foreignKeyValue, dataStore, context.getFieldNameResolver());
                if (context.getDataStoreUtil().isNew(entity)) {
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
