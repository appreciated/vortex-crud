package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;


import com.github.appreciated.turbo_crud.config.model.FormConfiguration;
import com.github.appreciated.turbo_crud.config.model.FormItem;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.item.DefaultCollectionItemImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.*;

public class DefaultCollectionFactoryImpl implements TurboCrudCollectionFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudDialogFactoryRegistry dialogFactory;

    public DefaultCollectionFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                        TurboCrudDialogFactoryRegistry dialogFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      Route route,
                                      FormItem factoryConfig,
                                      TurboCrudRouteFactoryRegistry routeFactory,
                                      FormCreator formCreator) {
        String table = factoryConfig.getTable();
        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(table);
        VerticalLayout list = new VerticalLayout();
        list.setPadding(false);
        list.setSpacing(false);
        list.getElement().setAttribute("theme", "spacing-s");
        list.getStyle().setMarginTop("calc(var(--lumo-font-size-s) * 1.5)");
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.add(new H4(list.getTranslation(factoryConfig.getLabel())));
        Button button = new Button(VaadinIcon.PLUS.create());
        button.addThemeVariants(LUMO_PRIMARY);
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, entityManagerFactoryRegistry, routeFactory, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, routeFactory, entityManagerFactoryRegistry, formCreator, list, header);
        return list;
    }

    private void loadCollection(String foreignKey,
                                FormItem formItem,
                                TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                                TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                FormCreator formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(formItem.getTable());
        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(formItem.getForeignKeyColumn(), foreignKey, 0, Integer.MAX_VALUE);
        for (GenericEntity record : recordsFromTableWhereColumnEquals) {
            DefaultCollectionItemImpl item = new DefaultCollectionItemImpl();
            item.getContent().addClickListener(event -> openDialog(EntityUtil.getId(record), foreignKey, formItem, entityManagerFactoryRegistry, routeFactoryRegistry, formCreator, list, header));

            Config configuration = formItem.getDialog().getChild().getConfiguration();
            FormConfiguration formConfiguration = ConfigBeanFactory.create(configuration, FormConfiguration.class);

            for (FormItem child : formConfiguration.getChildren()) {
                Object o = record.get(child.getColumn());
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    entityManagerService.deleteRecordById(EntityUtil.getId(record));
                    loadCollection(foreignKey, formItem, routeFactoryRegistry, entityManagerFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
        if (recordsFromTableWhereColumnEquals.isEmpty()) {
            list.add(new Text(list.getTranslation(formItem.getEmptyMessage())));
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            FormItem formItem,
                            TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                            TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                            FormCreator formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {

        Dialog dialog = dialogFactory.getFactory(formItem.getDialog().getFactory()).createDialog(
                entityId,
                foreignKey,
                formItem.getDialog().getChild(),
                formItem,
                routeFactoryRegistry,
                () -> loadCollection(foreignKey, formItem, routeFactoryRegistry, entityManagerFactoryRegistry, formCreator, list, header),
                formCreator);
        dialog.open();
    }
}