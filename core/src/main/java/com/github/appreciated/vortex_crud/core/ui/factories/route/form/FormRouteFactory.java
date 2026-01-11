package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.components.H2WithHasValue;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.github.appreciated.vortex_crud.core.exception.BusinessValidationException;
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;

/**
 * Default implementation of the {@link VortexCrudRouteFactory} interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class FormRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        @SuppressWarnings("unchecked")
        FormRouteProvider<ModelClass, FieldType, RepositoryType> routeProvider =
                (FormRouteProvider<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

        assert detailRouteSetting != null;
        return getForm(context, routeResolver, detailRouteSetting.isWrapped(), detailRouteSetting.isHeaderHidden(), detailRouteSetting.isCreationMode(), routeProvider.isDeleteButtonHidden(), routeProvider);
    }

    public VerticalLayout getForm(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                  VortexCrudPathToRouteResolver routeResolver,
                                  boolean isWrapped,
                                  boolean isHeaderHidden,
                                  boolean creationMode,
                                  boolean isDeleteButtonHidden,
                                  FormRouteProvider<ModelClass, FieldType, RepositoryType> routeRenderer) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        String title = routeRenderer.title();
        String prefix = !isWrapped ? (title != null ? layout.getTranslation(title) : "") + " / " : "";

        ReflectionService<FieldType> reflectionService = context.reflectionService();
        FormCreator<ModelClass, FieldType, RepositoryType> formCreator = context.formCreator();
        VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker = context.rbacPermissionChecker();

        H2WithHasValue titleComponent = new H2WithHasValue();
        Binder<Object> binder = new Binder<>(Object.class);
        if (!creationMode) {
            binder.bindReadOnly(
                    titleComponent,
                    entity1 -> prefix + reflectionService.getString(entity1, routeRenderer.titleField())
            );
        } else {
            titleComponent.setText(titleComponent.getTranslation("button.create.title"));
        }

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = routeRenderer.dataStoreConfig();
        RepositoryType table = tables.factory();
        VortexCrudDataStore<FieldType, ModelClass> dataStore = tables.dataStoreInstance();

        ModelClass entity;
        if (creationMode) {
            entity = dataStore.newInstance();
        } else if (routeRenderer instanceof SingleFormRoute) {
            // Root entry mode: fetch by filter instead of URL path ID
            SingleFormRoute<ModelClass, FieldType, RepositoryType> singleFormRoute =
                (SingleFormRoute<ModelClass, FieldType, RepositoryType>) routeRenderer;
            FieldType filterField = singleFormRoute.entityFilterField();
            Object filterValue = singleFormRoute.entityFilterValueProvider().get();

            java.util.List<ModelClass> results = dataStore.getRecordsFromTableWhereColumnEquals(
                filterField, filterValue, 0, 1);

            if (results.isEmpty()) {
                throw new IllegalStateException(
                    "No entity found for filter field: " + filterField +
                    " with value: " + filterValue);
            }
            entity = results.get(0);  // Take first result
        } else {
            // Traditional mode: fetch by ID from URL path
            String lastSegment = routeResolver.getLastSegment();
            entity = dataStore.getRecordById(lastSegment);
        }

        formCreator.bindAndAddToLayout(table, routeRenderer, routeRenderer.fields(), entity, context, tables, binder, form);
        binder.setBean(entity);

        // Generic Save button
        ComponentEventListener<ClickEvent<Button>> onSave = event -> {
            try {
                binder.writeBean(entity);
                if (!creationMode) {
                    dataStore.updateRecord(entity);
                    binder.setBean(entity);
                } else {
                    Object o = dataStore.insertRecord(entity);
                    binder.setBean(dataStore.getRecordById(o));
                }
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().getPage().getHistory().back();
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (BusinessValidationException e) {
                Notification notification = Notification.show(e.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        };

        ComponentEventListener<ClickEvent<Button>> onDelete = event -> {
            dataStore.deleteRecord(entity);
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
            UI.getCurrent().getPage().getHistory().back();
        };

        ComponentEventListener<ClickEvent<Button>> onBack = event -> UI.getCurrent().getPage().getHistory().back();

        // Check write permissions for save/delete buttons
        boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(routeRenderer, entity);

        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(
                isWrapped,
                creationMode && hasWriteAccess,
                hasWriteAccess ? onSave : null,
                null,
                hasWriteAccess && !isDeleteButtonHidden ? onDelete : null,
                onBack,
                titleComponent
        );
        if (!isHeaderHidden) {
            layout.add(headerBar);
        }
        layout.add(form);
        layout.setPadding(true);
        return layout;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
