package com.github.appreciated.turbo_crud.ui.factories.dialog;


import com.github.appreciated.turbo_crud.config.model.FormConfiguration;
import com.github.appreciated.turbo_crud.config.model.FormItem;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.config.model.TableConfig;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class DefaultDialogFactoryImpl implements TurboCrudDialogFactory {

    private final TurboCrudConfigService configService;
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private TurboCrudEntityManagerService entityManagerService;

    public DefaultDialogFactoryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        this.configService = configService;
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
    }

    @Override
    public Dialog createDialog(String entityId,
                               String foreignKeyValue,
                               Route route,
                               FormItem formItem,
                               TurboCrudRouteFactoryRegistry routeFactory,
                               OnStoreListener listener,
                               FormCreator formCreator) {
        String table = formItem.getTable();

        this.entityManagerService = entityManagerFactoryRegistry.getFactory(table);
        Dialog dialog = new Dialog();

        GenericEntity recordById = entityManagerService.getRecordById(entityId);
        if (recordById == null) {
            recordById = new GenericEntity();
        }

        if (EntityUtil.isNew(recordById)) {
            dialog.setHeaderTitle(dialog.getTranslation("button.create.title"));
        } else {
            dialog.setHeaderTitle(dialog.getTranslation("button.edit.title"));
        }

        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);
        binder.setBean(recordById);
        createFooter(foreignKeyValue, formItem, binder, recordById, dialog, listener);
        FormLayout layout = new FormLayout();

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(table);

        Config configuration = route.getConfiguration();
        FormConfiguration formConfiguration = ConfigBeanFactory.create(configuration, FormConfiguration.class);

        formCreator.bindAndAddToLayout(table, route, formConfiguration, recordById, routeFactory, tables, binder, layout, formCreator);

        dialog.add(layout);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(String foreignKeyValue, FormItem formItem, Binder<GenericEntity> binder, GenericEntity entity, Dialog dialog, OnStoreListener listener) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entity.put(formItem.getForeignKeyColumn(), foreignKeyValue);
                if (EntityUtil.isNew(entity)) {
                    entityManagerService.insertRecord(entity);
                } else {
                    entityManagerService.updateRecordById(EntityUtil.getId(entity), entity);
                }
                binder.setBean(entity);
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