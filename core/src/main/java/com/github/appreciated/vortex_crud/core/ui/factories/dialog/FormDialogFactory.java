package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
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

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private VortexCrudDataStore<FieldType, Object> dataStore;

    public FormDialogFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                             VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                             VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy,
                             VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.configService = configService;
        this.fieldNameResolver = fieldNameResolver;
        this.foreignKeyResolutionStrategy = foreignKeyResolutionStrategy;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Dialog create(@Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> config,
                         RepositoryType dataStoreKey,
                         OnStoreListener storeListener,
                         OnCancelListener onCancelListener,
                         FormCreator<ModelClass, FieldType, RepositoryType> formCreator) {

        this.dataStore = (VortexCrudDataStore<FieldType, Object>) configService.configuration().dataStores().get(dataStoreKey).dataStoreInstance();
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");

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

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = configService.configuration().dataStores().get(dataStoreKey);

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
                formCreator.bindAndAddToLayout(dataStoreKey, formRouteRenderer, childForm.children(), recordById, tables, binder, childFormLayout);
                formsContainer.add(childFormLayout);
            }
            formContent = formsContainer;
        } else {
            // Single form configuration
            FormLayout layout = new FormLayout();
            formCreator.bindAndAddToLayout(dataStoreKey, formRouteRenderer, configuration.children(), recordById, tables, binder, layout);
            formContent = layout;
        }

        binder.setBean(recordById);
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, storeListener, onCancelListener);

        dialog.add(formContent);
        dialog.setModality(VISUAL);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(Object foreignKeyValue, FieldType foreignKeyField, Binder<Object> binder, Object entity, Dialog dialog, OnStoreListener listener, OnCancelListener onCancelListener) {
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
