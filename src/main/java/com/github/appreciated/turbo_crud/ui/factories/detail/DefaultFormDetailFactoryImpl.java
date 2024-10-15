package com.github.appreciated.turbo_crud.ui.factories.detail;

import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.TableConfig;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.H2WithHasValue;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

/**
 * Default implementation of the {@link TurboCrudDetailFactory} interface.
 * This class handles rendering entity details in a form layout and provides functionalities
 * such as saving and deleting entities.
 */

public class DefaultFormDetailFactoryImpl implements TurboCrudDetailFactory {

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudConfigService configService;
    private final FormCreator formCreator;

    public DefaultFormDetailFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                        TurboCrudConfigService configService,
                                        FormCreator formCreator) {
        this.entityManagerService = entityManagerService;
        this.configService = configService;
        this.formCreator = formCreator;
    }

    @Override
    public Component renderDetail(String table,
                                  String title,
                                  DetailFactory detailFactory,
                                  GenericEntity entity,
                                  boolean isWrapped,
                                  boolean hideHeader,
                                  TurboCrudDetailFactoryRegistry detailFactoryRegistry) {
        H2WithHasValue titleComponent = new H2WithHasValue();

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        FormLayout form = new FormLayout();
        form.setMaxWidth("1000px");
        Binder<GenericEntity> binder = new Binder<>(GenericEntity.class);

        String prefix = !isWrapped ? layout.getTranslation(title) + " / " : "";

        binder.bind(
                titleComponent,
                entity1 -> prefix + entity1.getString(detailFactory.getTitleColumn()),
                (entity1, string) -> {
                }
        );

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(table);

        formCreator.bindAndAddToLayout(table, detailFactory, entity, detailFactoryRegistry, tables, binder, form, formCreator);

        binder.setBean(entity);

        // Generic Save button
        Button save = new Button(layout.getTranslation("button.save.title"), event -> {
            try {
                binder.writeBean(entity);
                entityManagerService.updateRecordById(table, EntityUtil.getId(entity), entity);
                binder.setBean(entity);
                Notification notification = Notification.show(layout.getTranslation("form.notification.successfully-saved"));
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (ValidationException e) {
                Notification notification = Notification.show(layout.getTranslation("form.notification.failed-to-save", e.getMessage()));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        save.addThemeVariants(LUMO_PRIMARY);

        // Generic Delete button
        Button delete = new Button(layout.getTranslation("button.delete.title"), event -> {
            entityManagerService.deleteRecordById(table, EntityUtil.getId(entity));
            Notification.show(layout.getTranslation("form.notification.successfully-deleted"));
        });
        delete.addThemeVariants(LUMO_ERROR);

        // Generic Delete button
        Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), event -> UI.getCurrent().getPage().getHistory().back());
        back.getStyle().set("font-size", "1.6em")
                .set("padding", "calc(var(--lumo-button-size) / 8)")
                .set("border-radius", "100%")
                .set("box-sizing", "content-box");

        HorizontalLayout headerBar = new HorizontalLayout();
        if (!isWrapped){
            headerBar.add(back);
        }

        // Add the form and buttons to the layout
        headerBar.add(titleComponent, save, delete);
        headerBar.setAlignItems(CENTER);
        if (!hideHeader) {
            layout.add(headerBar);
        }
        layout.add(form);
        layout.setPadding(true);
        return layout;
    }
}