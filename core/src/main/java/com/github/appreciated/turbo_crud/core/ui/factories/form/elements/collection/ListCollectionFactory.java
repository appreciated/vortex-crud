package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.turbo_crud.core.config.model.*;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.item.DefaultCollectionItem;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
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

public class ListCollectionFactory<DataStoreId, FieldId> implements TurboCrudCollectionFactory<DataStoreId, FieldId> {

    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactory;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> resolver;

    public ListCollectionFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                 TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactory,
                                 TurboCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.dialogFactory = dialogFactory;
        this.resolver = resolver;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      Route<DataStoreId, FieldId> route,
                                      InternalFormElement<DataStoreId, FieldId> factoryConfig,
                                      TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                                      FormCreator<DataStoreId, FieldId> formCreator) {
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
                                InternalFormElement<DataStoreId, FieldId> internalFormElement,
                                TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
                                TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                FormCreator<DataStoreId, FieldId> formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionData<DataStoreId, FieldId> data = internalFormElement.getConfiguration().getData();

        TurboCrudDataStore<FieldId> dataStore = dataStoreFactoryRegistry.getFactory(data.getDataStore());
        List<GenericEntity> records = getDataByConfig(foreignKeyValue, dataStore, data, dataStoreFactoryRegistry);
        if (internalFormElement.getConfiguration().getData().getOneToMany() != null) {
            addOneToManyItems(foreignKeyValue, internalFormElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header, records, dataStore);
        } else if (internalFormElement.getConfiguration().getData().getManyToMany() != null) {
            addManyToManyItems(foreignKeyValue, internalFormElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header, records, dataStore);
        } else {
            throw new IllegalArgumentException("No collection found for " + foreignKeyValue);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(internalFormElement.getConfiguration().getEmptyMessage())));
        }
    }

    private void addManyToManyItems(String foreignKeyValue, InternalFormElement<DataStoreId, FieldId>  internalFormElement, TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, FormCreator<DataStoreId, FieldId> formCreator, VerticalLayout list, HorizontalLayout header, List<GenericEntity> records, TurboCrudDataStore<FieldId> dataStore) {
        for (GenericEntity record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, internalFormElement, dataStoreFactoryRegistry, routeFactoryRegistry, formCreator, list, header));
            List<String> children = internalFormElement.getConfiguration().getData().getChildren();
            children.forEach(s -> item.addContent(new Text(record.getString(s))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(DataStoreUtil.getId(record));
                loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header);
            });
            list.add(item);
        }
    }

    private void addOneToManyItems(String foreignKeyValue, InternalFormElement<DataStoreId, FieldId> internalFormElement, TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, FormCreator<DataStoreId, FieldId> formCreator, VerticalLayout list, HorizontalLayout header, List<GenericEntity> records, TurboCrudDataStore<FieldId> dataStore) {
        for (GenericEntity record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, internalFormElement, dataStoreFactoryRegistry, routeFactoryRegistry, formCreator, list, header));
            RouteConfiguration<DataStoreId, FieldId> form = internalFormElement.getConfiguration().getChild().getConfiguration();
            for (InternalFormElement<DataStoreId, FieldId> child : form.getChildren()) {
                Object o = record.get(resolver.getKeyForFieldId(child.getField()));
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(DataStoreUtil.getId(record));
                    loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private List<GenericEntity> getDataByConfig(String foreignKeyValue, TurboCrudDataStore<FieldId> dataStore, CollectionData<DataStoreId, FieldId> collectionData, TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry) {
        if (collectionData.getOneToMany() != null) {
            return foreignKeyValue == null ? List.of() :
                    dataStore.getRecordsFromTableWhereColumnEquals(collectionData.getOneToMany().getReferenceField(), foreignKeyValue, 0, Integer.MAX_VALUE);
        } else if (collectionData.getManyToMany() != null) {
            // If we need to resolve a many-to-many relation it is necessary to do two selects one over the associative
            // datastore and one over the target datastore and one with the actual entries.
            // This could be improved upon, if it was allowed to provide a custom datastore / interface for the sake
            // of resolving the following data.
            ManyToMany<DataStoreId, FieldId> manyToMany = collectionData.getManyToMany();
            TurboCrudDataStore<FieldId> associativeDataStore = dataStoreFactoryRegistry.getFactory(manyToMany.getAssociativeDataStore());
            List<GenericEntity> associativeRecords = associativeDataStore.getRecordsFromTableWhereColumnEquals(manyToMany.getAssociativeSourceIdField(), foreignKeyValue, 0, Integer.MAX_VALUE);
            List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.get(resolver.getKeyForFieldId(manyToMany.getAssociativeTargetIdField()))).map(Object::toString).toList();
            return foreignKeyValue == null ? List.of() :
                    dataStore.getRecordsFromTableWhereColumnIn(manyToMany.getDataStoreField(), associativeRecordIds, 0, Integer.MAX_VALUE);
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            InternalFormElement<DataStoreId, FieldId> internalFormElement,
                            TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                            TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
                            FormCreator<DataStoreId, FieldId> formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection<DataStoreId, FieldId> collectionData = internalFormElement.getConfiguration();
        com.vaadin.flow.component.dialog.Dialog dialog = dialogFactory.getFactory(internalFormElement.getConfiguration().getFactory()).create(
                entityId,
                foreignKey,
                getReferenceField(collectionData.getData()),
                collectionData.getChild(),
                collectionData.getData(),
                collectionData.getData().getDataStore(),
                routeFactoryRegistry,
                () -> loadCollection(foreignKey, internalFormElement, routeFactoryRegistry, dataStoreFactoryRegistry, formCreator, list, header),
                formCreator);
        dialog.open();
    }

    private static <DataStoreId, FieldId> FieldId getReferenceField(CollectionData<DataStoreId, FieldId> collectionData) {
        if (collectionData.getOneToMany() != null) {
            OneToMany<FieldId> oneToMany = collectionData.getOneToMany();
            return oneToMany.getReferenceField();
        } else if (collectionData.getManyToMany() != null) {
            ManyToMany<DataStoreId, FieldId> oneToMany = collectionData.getManyToMany();
            return oneToMany.getDataStoreField();
        } else {
            throw new IllegalArgumentException("Either getOneToMany or getManyToMany must be specified");
        }
    }
}