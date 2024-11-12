package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.Form;
import com.github.appreciated.turbo_crud.config.model.Repository;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
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
import jakarta.annotation.Nullable;

public class DefaultDialogFactoryImpl implements TurboCrudDialogFactory {

    private final TurboCrudConfigService configService;
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private TurboCrudEntityManager entityManager;

    public DefaultDialogFactoryImpl(TurboCrudConfigService configService, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        this.configService = configService;
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
    }

    @Override
    public Dialog createDialog(@Nullable String entityId,
                               @Nullable String foreignKeyValue,
                               @Nullable String foreignKeyField,
                               Route formRoute,
                               String repository,
                               TurboCrudRouteFactoryRegistry routeFactory,
                               OnStoreListener listener,
                               FormCreator formCreator) {

        this.entityManager = entityManagerFactoryRegistry.getFactory(repository);
        Dialog dialog = new Dialog();

        GenericEntity recordById = entityManager.getRecordById(entityId);
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
        createFooter(foreignKeyValue, foreignKeyField, binder, recordById, dialog, listener);
        FormLayout layout = new FormLayout();

        Repository tables = configService.getConfiguration().getRepositoriesConfig().get(repository);

        Config configuration = formRoute.getConfiguration();
        Form form = ConfigBeanFactory.create(configuration, Form.class);

        formCreator.bindAndAddToLayout(repository, formRoute, form, recordById, routeFactory, tables, binder, layout, formCreator);

        dialog.add(layout);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }

    private void createFooter(String foreignKeyValue, String foreignKeyField, Binder<GenericEntity> binder, GenericEntity entity, Dialog dialog, OnStoreListener listener) {
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button saveButton = new Button(dialog.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                if (foreignKeyField != null && foreignKeyValue != null) {
                    entity.put(foreignKeyField, foreignKeyValue);
                }
                if (EntityUtil.isNew(entity)) {
                    entityManager.insertRecord(entity);
                } else {
                    entityManager.updateRecordById(EntityUtil.getId(entity), entity);
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