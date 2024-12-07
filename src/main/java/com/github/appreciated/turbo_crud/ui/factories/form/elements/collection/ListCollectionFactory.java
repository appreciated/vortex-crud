package com.github.appreciated.turbo_crud.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.item.DefaultCollectionItem;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.*;

public class ListCollectionFactory implements TurboCrudCollectionFactory {

    private final TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry;
    private final TurboCrudDialogFactoryRegistry dialogFactory;

    public ListCollectionFactory(TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                                 TurboCrudDialogFactoryRegistry dialogFactory) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      Route route,
                                      FormElement factoryConfig,
                                      TurboCrudRouteFactoryRegistry routeFactory,
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
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, dataStoreFactoryRegistry, routeFactory, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, routeFactory, dataStoreFactoryRegistry, formCreator, list, header);
        return list;
    }

    private void loadCollection(String foreignKeyValue,
                                FormElement formElement,
                                TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                                TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                                FormCreator formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionData data = formElement.getConfiguration().getData();

        TurboCrudDataStore dataStore = dataStoreFactoryRegistry.getFactory(data.getDataStore());
        List<GenericEntity> records = getDataByConfig(foreignKeyValue, dataStore, data, dataStoreFactoryRegistry);
        if (formElement.getConfiguration().getData().getOneToMany() != null) {
            addOneToManyItems(foreignKeyValue, formElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header, records, dataStore);
        } else if (formElement.getConfiguration().getData().getManyToMany() != null) {
            addManyToManyItems(foreignKeyValue, formElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header, records, dataStore);
        } else {
            throw new IllegalArgumentException("No collection found for " + foreignKeyValue);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(formElement.getConfiguration().getEmptyMessage())));
        }
    }

    private void addManyToManyItems(String foreignKeyValue, FormElement formElement, TurboCrudRouteFactoryRegistry routeFactoryRegistry, TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry, FormCreator formCreator, VerticalLayout list, HorizontalLayout header, List<GenericEntity> records, TurboCrudDataStore dataStore) {
        for (GenericEntity record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, formElement, dataStoreFactoryRegistry, routeFactoryRegistry, formCreator, list, header));
            List<String> children = formElement.getConfiguration().getData().getChildren();
            children.forEach(s -> item.addContent(new Text(record.getString(s))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(DataStoreUtil.getId(record));
                loadCollection(foreignKeyValue, formElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header);
            });
            list.add(item);
        }
    }

    private void addOneToManyItems(String foreignKeyValue, FormElement formElement, TurboCrudRouteFactoryRegistry routeFactoryRegistry, TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry, FormCreator formCreator, VerticalLayout list, HorizontalLayout header, List<GenericEntity> records, TurboCrudDataStore dataStore) {
        for (GenericEntity record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, formElement, dataStoreFactoryRegistry, routeFactoryRegistry, formCreator, list, header));
            RouteConfiguration form = formElement.getConfiguration().getChild().getConfiguration();
            for (FormElement child : form.getChildren()) {
                Object o = record.get(child.getField());
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(DataStoreUtil.getId(record));
                    loadCollection(foreignKeyValue, formElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private static List<GenericEntity> getDataByConfig(String foreignKeyValue, TurboCrudDataStore dataStore, CollectionData collectionData, TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry) {
        if (collectionData.getOneToMany() != null) {
            return foreignKeyValue == null ? List.of() :
                    dataStore.getRecordsFromTableWhereColumnEquals(collectionData.getOneToMany().getReferenceField(), foreignKeyValue, 0, Integer.MAX_VALUE);
        } else if (collectionData.getManyToMany() != null) {
            // If we need to resolve a many-to-many relation it is necessary to do two selects one over the associative
            // datastore and one over the target datastore and one with the actual entries.
            // This could be improved upon, if it was allowed to provide a custom datastore / interface for the sake
            // of resolving the following data.
            ManyToMany manyToMany = collectionData.getManyToMany();
            TurboCrudDataStore associativeDataStore = dataStoreFactoryRegistry.getFactory(manyToMany.getAssociativeDataStore());
            List<GenericEntity> associativeRecords = associativeDataStore.getRecordsFromTableWhereColumnEquals(manyToMany.getAssociativeSourceIdField(), foreignKeyValue, 0, Integer.MAX_VALUE);
            List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.get(manyToMany.getAssociativeTargetIdField())).map(Object::toString).toList();
            return foreignKeyValue == null ? List.of() :
                    dataStore.getRecordsFromTableWhereColumnIn(manyToMany.getDataStoreField(), associativeRecordIds, 0, Integer.MAX_VALUE);
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            FormElement formElement,
                            TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                            TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                            FormCreator formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection collectionData = formElement.getConfiguration();
        com.vaadin.flow.component.dialog.Dialog dialog = dialogFactory.getFactory(formElement.getConfiguration().getFactory()).create(
                entityId,
                foreignKey,
                getReferenceField(collectionData.getData()),
                collectionData.getChild(),
                collectionData.getData(),
                collectionData.getData().getDataStore(),
                routeFactoryRegistry,
                () -> loadCollection(foreignKey, formElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header),
                formCreator);
        dialog.open();
    }

    private static String getReferenceField(CollectionData collectionData) {
        if (collectionData.getOneToMany() != null) {
            OneToMany oneToMany = collectionData.getOneToMany();
            return oneToMany.getReferenceField();
        } else if (collectionData.getManyToMany() != null) {
            ManyToMany oneToMany = collectionData.getManyToMany();
            return oneToMany.getDataStoreField();
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }
}