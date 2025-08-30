package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.item.DefaultCollectionItem;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
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

public class ListCollectionFactory<DataStoreId, FieldId, KeyType> implements VortexCrudCollectionFactory<DataStoreId, FieldId, KeyType> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactory;
    private final ReflectionService<FieldId> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> manyToManyPersistenceStrategy;

    public ListCollectionFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                                 VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactory,
                                 ReflectionService<FieldId> reflectionService,
                                 VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                 ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> manyToManyPersistenceStrategy
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.dialogFactory = dialogFactory;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
    }

    @Override
    public Component createCollection(Object foreignKey,
                                      RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                                      InternalFormElement<DataStoreId, FieldId, KeyType> factoryConfig,
                                      VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                                      FormCreator<DataStoreId, FieldId, KeyType> formCreator) {
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
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, routeFactory, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, routeFactory, formCreator, list, header);
        return list;
    }

    private void loadCollection(Object foreignKeyValue,
                                InternalFormElement<DataStoreId, FieldId, KeyType> internalFormElement,
                                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                                FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionConfiguration<DataStoreId, FieldId, KeyType> data = internalFormElement.getConfiguration().getData();

        VortexCrudDataStore<FieldId, DataStoreId> dataStore = dataStoreFactoryRegistry.getDataStore(data.getDataStore());

        java.util.Collection<Object> records = (data.getManyToMany() != null) ?
                (java.util.Collection<Object>) manyToManyPersistenceStrategy.resolveManyToMany(dataStore, data.getManyToMany(), foreignKeyValue) :
                (java.util.Collection<Object>) data.getOneToMany().getData(foreignKeyValue, dataStore, data);

        if (internalFormElement.getConfiguration().getData().getOneToMany() != null) {
            addOneToManyItems(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header, records, dataStore);
        } else if (internalFormElement.getConfiguration().getData().getManyToMany() != null) {
            addManyToManyItems(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header, records, dataStore);
        } else {
            throw new IllegalArgumentException("No collection found for " + foreignKeyValue);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(internalFormElement.getConfiguration().getEmptyMessage())));
        }
    }

    private void addManyToManyItems(Object foreignKeyValue,
                                    InternalFormElement<DataStoreId, FieldId, KeyType> internalFormElement,
                                    VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                                    FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                                    VerticalLayout list,
                                    HorizontalLayout header,
                                    java.util.Collection<Object> records,
                                    VortexCrudDataStore<FieldId, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(foreignKeyValue, foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header));
            List<FieldId> children = internalFormElement.getConfiguration().getData().getChildren();
            children.forEach(fieldId -> item.addContent(new Text(reflectionService.getString(record, fieldId))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(dataStoreUtil.getId(record));
                loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header);
            });
            list.add(item);
        }
    }

    private void addOneToManyItems(Object foreignKeyValue,
                                   InternalFormElement<DataStoreId, FieldId, KeyType> internalFormElement,
                                   VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                                   FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                                   VerticalLayout list,
                                   HorizontalLayout header,
                                   java.util.Collection<Object> records,
                                   VortexCrudDataStore<FieldId, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(reflectionService.getId(record), foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header));
            @SuppressWarnings("unchecked")
            RouteRendererConfiguration<DataStoreId, FieldId, KeyType> form =
                    (RouteRendererConfiguration<DataStoreId, FieldId, KeyType>) internalFormElement.getConfiguration().getChild().getConfiguration();
            for (InternalFormElement<DataStoreId, FieldId, KeyType> child : form.getChildren()) {
                String textValue = reflectionService.getString(record, child.getField());
                item.addContent(new Text(textValue));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(dataStoreUtil.getId(record));
                    loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private void openDialog(Object entityId,
                            Object foreignKeyValue,
                            InternalFormElement<DataStoreId, FieldId, KeyType> internalFormElement,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                            FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection<DataStoreId, FieldId, KeyType> collectionData = internalFormElement.getConfiguration();
        CollectionConfiguration<DataStoreId, FieldId, KeyType> data = collectionData.getData();
        FieldId referenceField = (data.getManyToMany() != null) ?
                data.getManyToMany().getAssociativeSourceIdField() :
                data.getOneToMany().getReferenceField(data);

        com.vaadin.flow.component.dialog.Dialog dialog = dialogFactory.getFactory(internalFormElement.getConfiguration().getFactory()).create(
                entityId,
                foreignKeyValue,
                referenceField,
                collectionData.getChild(),
                collectionData.getData(),
                collectionData.getData().getDataStore(),
                routeFactoryRegistry,
                () -> loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header),
                () -> {
                },
                formCreator);
        dialog.open();
    }
}