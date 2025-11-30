package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
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

import static com.vaadin.flow.component.ModalityMode.VISUAL;

public class FormDialogFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {

    private VortexCrudDataStore<FieldType, Object> dataStore;

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
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");

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

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = context.configService().configuration().dataStores().get(dataStoreKey);

        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = formRouteRenderer.configuration();

        com.vaadin.flow.component.Component formContent;

        // Handle multi-form vs single-form configurations differently
        if (configuration instanceof MultiFormRendererConfiguration) {
            MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> multiFormConfig =
                (MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType>) configuration;

            com.vaadin.flow.component.html.Div formsContainer = new com.vaadin.flow.component.html.Div();

            // Create a separate FormLayout for each form in the multi-form configuration
            for (RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> childForm : multiFormConfig.forms()) {
                FormLayout childFormLayout = new FormLayout();
                childFormLayout.setMaxWidth("1000px");
                childFormLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
                );
                context.formCreator().bindAndAddToLayout(dataStoreKey, formRouteRenderer, childForm.children(), recordById, tables, binder, childFormLayout, context);
                formsContainer.add(childFormLayout);
            }
            formContent = formsContainer;
        } else {
            // Single form configuration
            FormLayout layout = new FormLayout();
            context.formCreator().bindAndAddToLayout(dataStoreKey, formRouteRenderer, configuration.children(), recordById, tables, binder, layout, context);
            formContent = layout;
        }

        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener, context);

        dialog.add(formContent);
        dialog.setModality(VISUAL);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, Object entity, Dialog dialog, OnStoreListener listener, OnCancelListener onCancelListener, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
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
                    } else {
                        throw new IllegalArgumentException("The given entity class (%s) does not match the datastore class model class (%s)".formatted(entity.getClass().getSimpleName(), dataStore.getModelClass().getSimpleName()));
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
