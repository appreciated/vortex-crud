package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.factories.collection.item.DefaultCollectionItemImpl;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.dialog.FlowCmsDialogFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
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

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;

public class DefaultCollectionFactoryImpl implements FlowCmsCollectionFactory {

    private final FlowCmsEntityManagerService entityManagerService;
    private final FlowCmsDialogFactoryRegistry dialogFactory;

    public DefaultCollectionFactoryImpl(FlowCmsEntityManagerService entityManagerService, FlowCmsDialogFactoryRegistry dialogFactory) {
        this.entityManagerService = entityManagerService;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public Component createCollection(String foreignKey, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator) {
        VerticalLayout list = new VerticalLayout();
        list.setPadding(false);
        list.getStyle().setMarginTop("calc(var(--lumo-font-size-s) * 1.5)");
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.add(new H4(list.getTranslation(factoryConfig.getLabel())));
        Button button = new Button(VaadinIcon.PLUS.create());
        button.addThemeVariants(LUMO_PRIMARY);
        button.addClickListener(event -> openDialog(foreignKey, factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header));
        header.add(button);
        init(foreignKey, factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header);
        return list;
    }

    private void openDialog(String foreignKey, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator, VerticalLayout list, HorizontalLayout header) {
        Dialog dialog = dialogFactory.getFactory(factoryConfig.getDialogFactory()).createDialog(null,
                factoryConfig,
                detailFactory,
                detailFactoryRegistry,
                () -> init(foreignKey, factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header),
                formCreator);
        dialog.open();
    }

    private void init(String foreignKey, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator, VerticalLayout list, HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(factoryConfig.getTable(), factoryConfig.getForeignKeyColumn(), foreignKey);
        for (GenericEntity record : recordsFromTableWhereColumnEquals) {
            DefaultCollectionItemImpl item = new DefaultCollectionItemImpl();
            item.addClickListener(event -> openDialog(""+record.get("id"), factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header));
            for (FormField child : factoryConfig.getChildren()) {
                Object o = record.get(child.getColumn());
                item.add(new Text(o.toString()));
            }
            list.add(item);
        }
        if (recordsFromTableWhereColumnEquals.isEmpty()) {
            list.add(new Text(list.getTranslation(factoryConfig.getEmptyMessage())));
        }
    }
}