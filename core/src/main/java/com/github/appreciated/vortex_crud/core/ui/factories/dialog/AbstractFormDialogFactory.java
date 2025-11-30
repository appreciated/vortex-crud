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

    protected VortexCrudDataStore<FieldType, Object> dataStore;

    @Override
    public Dialog create(@Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                         RepositoryType dataStoreKey,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener,
                         VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {

        this.dataStore = (VortexCrudDataStore<FieldType, Object>) context.configService().configuration().dataStores().get(dataStoreKey).dataStoreInstance();
        Dialog dialog = instantiateDialog();

        Object recordById = this.dataStore.getRecordById(entityId);
        if (recordById == null) {
            recordById = this.dataStore.newInstance();
        }

        if (context.dataStoreUtil().isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<Object> binder = new Binder<>(Object.class);
        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener, context);
        FormLayout layout = new FormLayout();

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = context.configService().configuration().dataStores().get(dataStoreKey);

        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = formRouteRenderer.configuration();
        context.formCreator().bindAndAddToLayout(dataStoreKey, formRouteRenderer, configuration.children(), recordById,
                tables, binder, layout, context);

        dialog.add(layout);
        return dialog;
    }

    protected abstract Dialog instantiateDialog();

    private void createFooter(Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, Object entity, Dialog dialog,
                              OnStoreListener listener, OnCancelListener onCancelListener, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> {
            onCancelListener.onCancel();
            dialog.close();
        });
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                context.foreignKeyResolutionStrategy().resolveForeignKey(entity, foreignKeyField, foreignKeyValue, dataStore, context.fieldNameResolver());
                if (context.dataStoreUtil().isNew(entity)) {
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
