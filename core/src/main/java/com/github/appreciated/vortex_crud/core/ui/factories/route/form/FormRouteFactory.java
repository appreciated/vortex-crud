package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.H2WithHasValue;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
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
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;

/**
 * Default implementation of the {@link VortexCrudRouteFactory} interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class FormRouteFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public FormRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                            VortexCrudConfigService<DataStoreId, FieldId> configService,
                            FormCreator<DataStoreId, FieldId> formCreator,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.formCreator = formCreator;
        this.factoryRegistry = factoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        RouteRenderer<DataStoreId, FieldId> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);
        RouteRendererConfiguration<DataStoreId, FieldId> form = routeRenderer.getConfiguration();
        assert detailRouteSetting != null;
        return getForm(routeResolver, detailRouteSetting.isWrapped(), detailRouteSetting.isHeaderHidden(), detailRouteSetting.isCreationMode(), routeRenderer, form);
    }

    public VerticalLayout getForm(VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver, boolean isWrapped, boolean isHeaderHidden, boolean creationMode, RouteRenderer<DataStoreId, FieldId> routeRenderer, RouteRendererConfiguration<DataStoreId, FieldId> formRouteRendererConfiguration) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        String prefix = !isWrapped ? layout.getTranslation(routeRenderer.getTitle()) + " / " : "";

        H2WithHasValue titleComponent = new H2WithHasValue();
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        if (!creationMode) {
            binder.bind(
                    titleComponent,
                    entity1 -> prefix + entity1.getString(fieldNameResolver.getKeyForFieldId(formRouteRendererConfiguration.getTitleField())),
                    (entity1, string) -> {
                    }
            );
        } else {
            titleComponent.setText(titleComponent.getTranslation("button.create.title"));
        }

        DataStoreId table = routeRenderer.getDataStore();
        DataStoreConfig<DataStoreId, FieldId> tables = configService.getConfiguration().getDataStores().get(table);
        String lastSegment = routeResolver.getLastSegment();
        VortexCrudDataStore<FieldId> dataStore = dataStoreFactoryRegistry.getFactory(table);
        GenericEntity entity = creationMode ? new GenericEntity() : dataStore.getRecordById(lastSegment);
        formCreator.bindAndAddToLayout(table, routeRenderer, formRouteRendererConfiguration, entity, factoryRegistry, tables, binder, form, formCreator);
        binder.setBean(entity);

        // Generic Save button
        ComponentEventListener<ClickEvent<Button>> onSave = event -> {
            try {
                binder.writeBean(entity);
                if (!creationMode) {
                    dataStore.updateRecordById(DataStoreUtil.getId(entity), entity);
                    binder.setBean(entity);
                } else {
                    Object o = dataStore.insertRecord(entity);
                    binder.setBean(dataStore.getRecordById(o));
                }
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        };

        ComponentEventListener<ClickEvent<Button>> onDelete = event -> {
            dataStore.deleteRecordById(DataStoreUtil.getId(entity));
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
        };

        ComponentEventListener<ClickEvent<Button>> onBack = event -> UI.getCurrent().getPage().getHistory().back();

        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(isWrapped, creationMode, onSave, null, onDelete, onBack, titleComponent);
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