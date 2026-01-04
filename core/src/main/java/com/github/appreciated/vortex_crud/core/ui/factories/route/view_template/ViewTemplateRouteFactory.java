package com.github.appreciated.vortex_crud.core.ui.factories.route.view_template;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.ViewTemplateRoute;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ViewTemplateRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        ViewTemplateRoute<ModelClass, FieldType, RepositoryType> route = (ViewTemplateRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);
        return new ViewTemplateView<>(route, context);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

    private static class ViewTemplateView<ModelClass, FieldType, RepositoryType> extends Div implements TemplateBindingContext<ModelClass, FieldType> {
        private final ViewTemplateRoute<ModelClass, FieldType, RepositoryType> route;
        private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
        private final ModelClass entity;
        private final Binder<ModelClass> binder;
        private boolean editMode = false;
        private final List<Component> boundComponents = new ArrayList<>();
        private final List<Button> actionButtons = new ArrayList<>();
        private Button editButton;
        private Button saveButton;
        private Button cancelButton;
        private Component header;
        private Div actionsContainer;

        public ViewTemplateView(ViewTemplateRoute<ModelClass, FieldType, RepositoryType> route, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
            this.route = route;
            this.context = context;
            setSizeFull();

            DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = route.dataStoreConfig();
            VortexCrudDataStore<FieldType, ModelClass> dataStore = dataStoreConfig.dataStoreInstance();
            FieldType filterField = route.entityFilterField();
            Object filterValue = route.entityFilterValueProvider() != null ? route.entityFilterValueProvider().get() : null;

            if (filterField != null && filterValue != null) {
                 java.util.List<ModelClass> results = dataStore.getRecordsFromTableWhereColumnEquals(filterField, filterValue, 0, 1);
                if (results.isEmpty()) {
                    try {
                        entity = dataStore.newInstance();
                         try {
                            context.reflectionService().setValue(entity, filterField, filterValue);
                         } catch (Exception e) {
                             log.warn("Could not set filter value on new entity", e);
                         }
                    } catch (Exception e) {
                        throw new IllegalStateException("Entity not found and could not create new one", e);
                    }
                } else {
                    entity = results.get(0);
                }
            } else {
                 try {
                    entity = dataStore.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Could not create new entity", e);
                }
            }

            binder = new Binder<>();

            initActions();

            Component layout = route.routeTemplateProvider().createLayout(entity, this);
            add(layout);

            binder.setBean(entity);
            updateEditMode();
        }

        private void initActions() {
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker = context.rbacPermissionChecker();
            boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(route, entity);

            editButton = new Button(VaadinIcon.EDIT.create(), event -> setEditMode(true));
            saveButton = new Button(VaadinIcon.CHECK.create(), event -> triggerSave());
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> cancel());

            actionsContainer = new Div();
            actionsContainer.add(editButton, saveButton, cancelButton);

            // Initial visibility
            editButton.setVisible(hasWriteAccess);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
        }

        @Override
        public Component getComponent(FieldType fieldKey) {
            DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig = route.dataStoreConfig();
            Field<ModelClass, FieldType, RepositoryType> fieldConfig = dataStoreConfig.fields().get(fieldKey);
            if (fieldConfig == null) {
                return new Div(); // Or error message
            }
            VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = fieldConfig.factory();

            Component component = factory.createComponent(dataStoreConfig.factory(), fieldKey, fieldConfig, context);

            if (component instanceof HasValue) {
                binder.bind((HasValue) component,
                    item -> context.reflectionService().getValue(item, fieldKey),
                    (item, value) -> context.reflectionService().setValue(item, fieldKey, value)
                );
            }

            if (component instanceof HasValueAndElement) {
                ((HasValueAndElement<?, ?>) component).setReadOnly(true); // Default to read-only
                boundComponents.add(component);
            }

            return component;
        }

        @Override
        public Binder<ModelClass> getBinder() {
            return binder;
        }

        @Override
        public void triggerSave() {
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
            binder.readBean(entity);
            setEditMode(false);
        }

        private void setEditMode(boolean edit) {
            this.editMode = edit;
            for (Component component : boundComponents) {
                 if (component instanceof HasValueAndElement) {
                    ((HasValueAndElement<?, ?>) component).setReadOnly(!edit);
                }
            }
            updateEditMode();
        }

        private void updateEditMode() {
             VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker = context.rbacPermissionChecker();
            boolean hasWriteAccess = permissionChecker == null || permissionChecker.hasUserWriteAccessToRoute(route, entity);

            if (editButton != null) editButton.setVisible(!editMode && hasWriteAccess);
            if (saveButton != null) saveButton.setVisible(editMode);
            if (cancelButton != null) cancelButton.setVisible(editMode);
        }

        @Override
        public Component getActionButtons() {
            return actionsContainer;
        }

        @Override
        public Component getHeader() {
            if (header == null) {
                HorizontalLayout h = new HorizontalLayout();
                h.setWidthFull();
                h.setPadding(true);
                h.setAlignItems(HorizontalLayout.Alignment.CENTER);

                H2 title = new H2(route.title() != null ? getTranslation(route.title()) : "");
                h.add(title);
                h.expand(title);
                header = h;
            }
            return header;
        }
    }
}
