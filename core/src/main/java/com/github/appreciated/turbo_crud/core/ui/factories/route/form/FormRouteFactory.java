package com.github.appreciated.turbo_crud.core.ui.factories.route.form;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.config.model.RouteConfiguration;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
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
 * Default implementation of the {@link TurboCrudRouteFactory} interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class FormRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public FormRouteFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                            TurboCrudConfigService<DataStoreId, FieldId> configService,
                            FormCreator<DataStoreId, FieldId> formCreator,
                            TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry,
                            TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
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
            TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        Route<DataStoreId, FieldId> route = routeResolver.getRouteForIndex(currentPathIndex);
        RouteConfiguration<DataStoreId, FieldId> form = route.getConfiguration();
        assert detailRouteSetting != null;
        return getForm(routeResolver, detailRouteSetting.isWrapped(), detailRouteSetting.isHeaderHidden(), detailRouteSetting.isCreationMode(), route, form);
    }

    public VerticalLayout getForm(TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver, boolean isWrapped, boolean isHeaderHidden, boolean creationMode, Route<DataStoreId, FieldId> route, RouteConfiguration<DataStoreId, FieldId> formRouteConfiguration) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("250px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        String prefix = !isWrapped ? layout.getTranslation(route.getTitle()) + " / " : "";

        H2WithHasValue titleComponent = new H2WithHasValue();
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        if (!creationMode) {
            binder.bind(
                    titleComponent,
                    entity1 -> prefix + entity1.getString(fieldNameResolver.getKeyForFieldId(formRouteConfiguration.getTitleField())),
                    (entity1, string) -> {
                    }
            );
        } else {
            titleComponent.setText(titleComponent.getTranslation("button.create.title"));
        }

        DataStoreId table = route.getDataStore();
        DataStoreConfig<DataStoreId, FieldId> tables = configService.getConfiguration().getDataStores().get(table);
        String lastSegment = routeResolver.getLastSegment();
        TurboCrudDataStore<FieldId> dataStore = dataStoreFactoryRegistry.getFactory(table);
        GenericEntity entity = creationMode ? new GenericEntity() : dataStore.getRecordById(lastSegment);
        formCreator.bindAndAddToLayout(table, route, formRouteConfiguration, entity, factoryRegistry, tables, binder, form, formCreator);
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