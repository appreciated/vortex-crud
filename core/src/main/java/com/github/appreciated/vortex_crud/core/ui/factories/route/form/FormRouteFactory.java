package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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

public class FormRouteFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final FormCreator<DataStoreId, FieldId, KeyType> formCreator;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> factoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ReflectionService<FieldId> reflectionService;

    public FormRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                            VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                            FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> factoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                            ReflectionService<FieldId> reflectionService
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.formCreator = formCreator;
        this.factoryRegistry = factoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);
        @SuppressWarnings("unchecked")
        RouteRendererConfiguration<DataStoreId, FieldId, KeyType> form = detailRouteSetting.isCreationMode()
                ? (RouteRendererConfiguration<DataStoreId, FieldId, KeyType>) routeRenderer.getChild().getConfiguration()
                : (RouteRendererConfiguration<DataStoreId, FieldId, KeyType>) routeRenderer.getConfiguration();
        assert detailRouteSetting != null;
        return getForm(routeResolver, detailRouteSetting.isWrapped(), detailRouteSetting.isHeaderHidden(), detailRouteSetting.isCreationMode(), routeRenderer, form);
    }

    public VerticalLayout getForm(VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                                  boolean isWrapped,
                                  boolean isHeaderHidden,
                                  boolean creationMode,
                                  RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                                  RouteRendererConfiguration<DataStoreId, FieldId, KeyType> formRouteRendererConfiguration) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        String prefix = !isWrapped ? layout.getTranslation(routeRenderer.getTitle()) + " / " : "";

        H2WithHasValue titleComponent = new H2WithHasValue();
        Binder<Object> binder = new Binder<>(Object.class);
        if (!creationMode) {
            binder.bindReadOnly(
                    titleComponent,
                    entity1 -> prefix + reflectionService.getString(entity1, formRouteRendererConfiguration.getTitleField())
            );
        } else {
            titleComponent.setText(titleComponent.getTranslation("button.create.title"));
        }

        KeyType table = routeRenderer.getDataStoreKey();
        DataStoreConfig<DataStoreId, FieldId, KeyType> tables = configService.getConfiguration().getDataStores().get(table);
        String lastSegment = routeResolver.getLastSegment();
        VortexCrudDataStore<FieldId, DataStoreId> dataStore = dataStoreFactoryRegistry.getDataStore(table);
        DataStoreId entity = creationMode ? dataStore.newInstance() : dataStore.getRecordById(lastSegment);
        formCreator.bindAndAddToLayout(table, routeRenderer, formRouteRendererConfiguration, entity, factoryRegistry, tables, binder, form, formCreator);
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