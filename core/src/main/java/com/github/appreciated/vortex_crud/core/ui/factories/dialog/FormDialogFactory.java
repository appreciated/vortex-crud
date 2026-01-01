package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
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

    @Override
    public Dialog create(@Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                         VortexCrudDataStore<FieldType, ModelClass> dataStore,
                         VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                         DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener) {

        VortexCrudDataStoreUtilStrategy dataStoreUtil = context.dataStoreUtil();
        FormCreator<ModelClass, FieldType, RepositoryType> formCreator = context.formCreator();

        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");

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

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = dataStoreConfig != null ? dataStoreConfig : formRouteRenderer.dataStoreConfig();
        RepositoryType dataStoreKey = tables.factory();

        com.vaadin.flow.component.Component formContent;

        // Handle multi-form vs single-form configurations differently
        if (formRouteRenderer instanceof MultiFormRoute) {
            MultiFormRoute<ModelClass, FieldType, RepositoryType> multiFormRoute =
                (MultiFormRoute<ModelClass, FieldType, RepositoryType>) formRouteRenderer;

            com.vaadin.flow.component.html.Div formsContainer = new com.vaadin.flow.component.html.Div();

            // Create a separate FormLayout for each form in the multi-form configuration
            for (RouteRenderer<ModelClass, FieldType, RepositoryType> childForm : multiFormRoute.forms()) {
                FormLayout childFormLayout = new FormLayout();
                childFormLayout.setMaxWidth("1000px");
                childFormLayout.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
                );
                formCreator.bindAndAddToLayout(dataStoreKey, formRouteRenderer, childForm.children(), recordById, context, tables, binder, childFormLayout);
                formsContainer.add(childFormLayout);
            }
            formContent = formsContainer;
        } else {
            // Single form configuration
            FormLayout layout = new FormLayout();
            formCreator.bindAndAddToLayout(dataStoreKey, formRouteRenderer, formRouteRenderer.children(), recordById, context, tables, binder, layout);
            formContent = layout;
        }

        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener, context, dataStore);

        dialog.add(formContent);
        dialog.setModality(VISUAL);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, ModelClass entity, Dialog dialog, OnStoreListener listener, OnCancelListener onCancelListener, VortexCrudContext<ModelClass, FieldType, RepositoryType> context, VortexCrudDataStore<FieldType, ModelClass> dataStore) {

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
