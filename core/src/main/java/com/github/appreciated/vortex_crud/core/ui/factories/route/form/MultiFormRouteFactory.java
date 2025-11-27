package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.MultiFormRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.MultiFormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.H2WithHasValue;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;

public class MultiFormRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final FormRouteFactory<ModelClass, FieldType, RepositoryType> formRouteFactory;

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;

    private String titleColumn;

    public MultiFormRouteFactory(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldType> reflectionService,
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker
    ) {
        this.configService = configService;
        this.formCreator = formCreator;
        this.reflectionService = reflectionService;
        this.permissionChecker = permissionChecker;
        this.formRouteFactory = new FormRouteFactory<>(configService, formCreator, reflectionService, permissionChecker);
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        assert routeResolver.getRouteForIndex(currentPathIndex) instanceof MultiFormRoute<ModelClass, FieldType, RepositoryType>;
        MultiFormRoute<ModelClass, FieldType, RepositoryType> routeProvider = (MultiFormRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);
        assert detailRouteSetting != null;
        return getForm(routeResolver, detailRouteSetting.isWrapped(), detailRouteSetting.isHeaderHidden(), detailRouteSetting.isCreationMode(), false, routeProvider);
    }

    public VerticalLayout getForm(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                  boolean isWrapped,
                                  boolean isHeaderHidden,
                                  boolean creationMode,
                                  boolean isDeleteButtonHidden,
                                  MultiFormRoute<ModelClass, FieldType, RepositoryType> routeRenderer) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        String prefix = !isWrapped ? layout.getTranslation(routeRenderer.title()) + " / " : "";

        H2WithHasValue titleComponent = new H2WithHasValue();
        Binder<Object> binder = new Binder<>(Object.class);
        MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration =
                (MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration();
        if (!creationMode) {
            // Use titleField from multi-form config, or fall back to first form's titleField
            FieldType titleField = formConfiguration.titleField();
            if (titleField == null && !formConfiguration.forms().isEmpty()) {
                titleField = formConfiguration.forms().get(0).titleField();
            }
            if (titleField != null) {
                FieldType finalTitleField = titleField;
                binder.bindReadOnly(
                        titleComponent,
                        entity1 -> prefix + reflectionService.getString(entity1, finalTitleField)
                );
            }
        } else {
            titleComponent.setText(titleComponent.getTranslation("button.create.title"));
        }

        RepositoryType table = routeRenderer.dataStoreKey();
        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = configService.configuration().dataStores().get(table);
        VortexCrudDataStore<FieldType, ModelClass> dataStore = tables.dataStoreInstance();

        ModelClass entity;
        if (creationMode) {
            entity = dataStore.newInstance();
        } else {
            // Traditional mode: fetch by ID from URL path
            String lastSegment = routeResolver.getLastSegment();
            entity = dataStore.getRecordById(lastSegment);
        }

        Div Forms = new Div();
        for (RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> child : formConfiguration.forms()) {
            com.vaadin.flow.component.formlayout.FormLayout childFormLayout = new com.vaadin.flow.component.formlayout.FormLayout();
            childFormLayout.setMaxWidth("1000px");
            childFormLayout.setResponsiveSteps(new com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep("250px", 2, com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.TOP));
            formCreator.bindAndAddToLayout(table, routeRenderer, child.children(), entity, tables, binder, childFormLayout);
            Forms.add(childFormLayout);
        }

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
            }
        };

        ComponentEventListener<ClickEvent<Button>> onDelete = event -> {
            dataStore.deleteRecord(entity);
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
            UI.getCurrent().getPage().getHistory().back();
        };

        ComponentEventListener<ClickEvent<Button>> onBack = event -> UI.getCurrent().getPage().getHistory().back();

        // Check write permissions for save/delete buttons
        boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(routeRenderer);

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
        layout.add(Forms);
        layout.setPadding(true);
        return layout;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
