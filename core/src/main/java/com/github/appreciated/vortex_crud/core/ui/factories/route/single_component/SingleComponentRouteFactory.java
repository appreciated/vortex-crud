package com.github.appreciated.vortex_crud.core.ui.factories.route.single_component;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreDropdownMenuAction;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.SingleComponentRoute;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreAdapter;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.actions.DataStoreDropdownMenuActionComponent;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SingleComponentRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        SingleComponentRoute<ModelClass, FieldType, RepositoryType> route = (SingleComponentRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);
        return new SingleComponentView<>(route, context);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

    private static class SingleComponentView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
        private final SingleComponentRoute<ModelClass, FieldType, RepositoryType> route;
        private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
        private final ModelClass entity;
        private final Binder<ModelClass> binder;
        private boolean editMode = false;
        private Component editorComponent;
        private Button editButton;
        private Button saveButton;
        private Button cancelButton;
        private HorizontalLayout header;

        public SingleComponentView(SingleComponentRoute<ModelClass, FieldType, RepositoryType> route, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
            this.route = route;
            this.context = context;
            setSizeFull();
            setPadding(false);
            setSpacing(false);

            DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = route.dataStoreConfig();
            VortexCrudDataStore<FieldType, ModelClass> dataStore = dataStoreConfig.dataStoreInstance();

            VortexCrudQueryDataStore<FieldType, ModelClass> queryDataStore =
                    (dataStore instanceof VortexCrudQueryDataStore)
                            ? (VortexCrudQueryDataStore<FieldType, ModelClass>) dataStore
                            : new VortexCrudQueryDataStoreAdapter<>(dataStore);

            FieldType filterField = route.entityFilterField();
            Object filterValue = route.entityFilterValueProvider().get();
            java.util.List<ModelClass> results = queryDataStore.getRecordsFromTableWhereColumnEquals(filterField, filterValue, 0, 1);
            if (results.isEmpty()) {
                try {
                    entity = dataStore.newInstance();
                    if (filterField != null && filterValue != null) {
                         try {
                            context.reflectionService().setValue(entity, filterField, filterValue);
                         } catch (Exception e) {
                             log.warn("Could not set filter value on new entity", e);
                         }
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Entity not found and could not create new one", e);
                }
            } else {
                entity = results.get(0);
            }

            binder = new Binder<>();
            initUI();
        }

        private void initUI() {
            FieldType fieldKey = route.field();
            DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = route.dataStoreConfig();
            Field<ModelClass, FieldType, RepositoryType> fieldConfig = dataStoreConfig.fields().get(fieldKey);
            VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = fieldConfig.factory();
            
            // Create the component
            editorComponent = factory.createComponent(dataStoreConfig.factory(), fieldKey, fieldConfig, context);
            
            if (editorComponent instanceof HasValue) {
                binder.bind((HasValue) editorComponent, 
                    item -> context.reflectionService().getValue(item, fieldKey), 
                    (item, value) -> context.reflectionService().setValue(item, fieldKey, value)
                );
                binder.setBean(entity);
            } else {
                log.warn("Component for field {} is not HasValue, cannot bind.", fieldKey);
            }
            
            // Layout Structure
            // Header
            header = new HorizontalLayout();
            header.setWidthFull();
            header.setPadding(true);
            header.setAlignItems(Alignment.CENTER);
            
            H2 title = new H2(getTranslation(route.title()));
            header.add(title);
            header.expand(title);

            // Menu Actions
            List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions = route.menuActions();
            if (menuActions != null) {
                for (DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType> action : menuActions) {
                     DataStoreDropdownMenuActionComponent<ModelClass, FieldType, RepositoryType> actionComponent = 
                        new DataStoreDropdownMenuActionComponent<>(action, entity, context, route);
                     header.add(actionComponent);
                }
            }

            // Roles Logic
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker = context.rbacPermissionChecker();
            boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(route, entity);
            
            editButton = new Button(VaadinIcon.EDIT.create(), event -> setEditMode(true));
            saveButton = new Button(VaadinIcon.CHECK.create(), event -> save());
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> cancel());
            
            // Edit button visible only if user has write access
            editButton.setVisible(hasWriteAccess);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);

            header.add(editButton, saveButton, cancelButton);
            
            add(header);
            
            // Editor Component Container
            VerticalLayout content = new VerticalLayout(editorComponent);
            content.setSizeFull();
            content.setPadding(true);
            add(content);
            expand(content);
            
            if (editorComponent instanceof HasValueAndElement) {
                ((HasValueAndElement<?, ?>) editorComponent).setReadOnly(true);
            }

            setEditMode(false);
        }

        private void setEditMode(boolean edit) {
            this.editMode = edit;
            if (editorComponent instanceof HasValueAndElement) {
                ((HasValueAndElement<?, ?>) editorComponent).setReadOnly(!edit);
            }
            
            // Edit Button should reappear only if user has write access and we are leaving edit mode
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker = context.rbacPermissionChecker();
            boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(route, entity);

            editButton.setVisible(!edit && hasWriteAccess);
            saveButton.setVisible(edit);
            cancelButton.setVisible(edit);
        }

        private void save() {
            try {
                binder.writeBean(entity);
                DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = route.dataStoreConfig();
                VortexCrudDataStore<FieldType, ModelClass> dataStore = dataStoreConfig.dataStoreInstance();
                
                Object id = context.reflectionService().getId(entity);
                if (id == null) {
                    dataStore.insertRecord(entity);
                } else {
                    dataStore.updateRecord(entity);
                }

                Notification.show(getTranslation("form.notification.successfully-saved")).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                setEditMode(false);
            } catch (ValidationException e) {
                 Notification.show(getTranslation("form.notification.failed-to-save", e.getMessage())).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception e) {
                 Notification.show(getTranslation("form.notification.failed-to-save", e.getMessage())).addThemeVariants(NotificationVariant.LUMO_ERROR);
                 log.error("Save failed", e);
            }
        }
        
        private void cancel() {
            binder.readBean(entity); // Revert changes
            setEditMode(false);
        }
    }
}
