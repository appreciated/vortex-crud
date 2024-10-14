package com.github.appreciated.turbo_crud.ui.factories.elements.collection;

import com.github.appreciated.turbo_crud.config.model.DialogConfig;
import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.elements.collection.item.DefaultCollectionItemImpl;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
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

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudDialogFactoryRegistry dialogFactory;

    public DefaultCollectionFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                        TurboCrudDialogFactoryRegistry dialogFactory) {
        this.entityManagerService = entityManagerService;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      FormElement factoryConfig,
                                      TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                                      DetailFactory detailFactory,
                                      FormCreator formCreator) {
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
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, detailFactoryRegistry, detailFactory, formCreator, list, header);
        return list;
    }

    private void loadCollection(String foreignKey,
                                FormElement formElement,
                                TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                                DetailFactory detailFactory,
                                FormCreator formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(formElement.getTable(), formElement.getForeignKeyColumn(), foreignKey);
        for (GenericEntity record : recordsFromTableWhereColumnEquals) {
            DefaultCollectionItemImpl item = new DefaultCollectionItemImpl();
            item.getContent().addClickListener(event -> openDialog(EntityUtil.getId(record), foreignKey, formElement, detailFactoryRegistry, detailFactory, formCreator, list, header));
            for (FormElement child : formElement.getDialog().getDetail().getChildren()) {
                Object o = record.get(child.getColumn());
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    entityManagerService.deleteRecordById(formElement.getTable(), EntityUtil.getId(record));
                    loadCollection(foreignKey, formElement, detailFactoryRegistry, detailFactory, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
        if (recordsFromTableWhereColumnEquals.isEmpty()) {
            list.add(new Text(list.getTranslation(formElement.getEmptyMessage())));
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            FormElement formElement,
                            TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                            DetailFactory detailFactory,
                            FormCreator formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Dialog dialog = dialogFactory.getFactory(formElement.getFactory()).createDialog(
                entityId,
                foreignKey,
                formElement,
                detailFactory,
                detailFactoryRegistry,
                () -> loadCollection(foreignKey, formElement, detailFactoryRegistry, detailFactory, formCreator, list, header),
                formCreator);
        dialog.open();
    }
}