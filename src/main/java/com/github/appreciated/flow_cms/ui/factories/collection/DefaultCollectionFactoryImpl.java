package com.github.appreciated.flow_cms.ui.factories.collection;

import com.github.appreciated.flow_cms.config.model.CollectionFactoryConfig;
import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
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
    public Component createCollection(String id, CollectionFactoryConfig factoryConfig, FlowCmsDetailFactoryRegistry detailFactoryRegistry, DetailFactory detailFactory, FormCreator formCreator) {
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
        button.addClickListener(event -> {
            Dialog dialog = dialogFactory.getFactory(factoryConfig.getDialogFactory()).createDialog(null,
                    factoryConfig,
                    detailFactory,
                    detailFactoryRegistry,
                    () -> init(id, factoryConfig, list, header),
                    formCreator);
            dialog.open();
        });
        header.add(button);
        init(id, factoryConfig, list, header);
        return list;
    }

    private void init(String id, CollectionFactoryConfig factoryConfig, VerticalLayout list, HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(factoryConfig.getTable(), factoryConfig.getForeignKeyColumn(), id);
        for (GenericEntity recordsFromTableWhereColumnEqual : recordsFromTableWhereColumnEquals) {
            HorizontalLayout item = new HorizontalLayout();
            for (FormField child : factoryConfig.getChildren()) {
                Object o = recordsFromTableWhereColumnEqual.get(child.getColumn());
                item.add(new Text(o.toString()));
            }
        }
        if (recordsFromTableWhereColumnEquals.isEmpty()) {
            list.add(new Text("Keine Kommentare vorhanden, klicken Sie auf '+' um Elemente hinzuzufügen"));
        }
    }
}