package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.item.DefaultCollectionItem;
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

public class ListCollectionFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy;

    public ListCollectionFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                 ReflectionService<FieldType> reflectionService,
                                 VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                 ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy
    ) {
        this.configService = configService;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
    }

    @Override
    public Component createCollection(Object foreignKey,
                                      RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                      InternalFormElement<ModelClass, FieldType, RepositoryType> factoryConfig,
                                      FormCreator<ModelClass, FieldType, RepositoryType> formCreator) {
        VerticalLayout list = new VerticalLayout();
        list.setPadding(false);
        list.setSpacing(false);
        list.getElement().setAttribute("theme", "spacing-s");
        list.getStyle().setMarginTop("calc(var(--lumo-font-size-s) * 1.5)");
        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.add(new H4(list.getTranslation(factoryConfig.label())));
        Button button = new Button(VaadinIcon.PLUS.create());
        button.addThemeVariants(LUMO_PRIMARY);
        button.addClickListener(event -> openDialog(null, foreignKey, factoryConfig, formCreator, list, header));
        header.add(button);
        loadCollection(foreignKey, factoryConfig, formCreator, list, header);
        return list;
    }

    private void loadCollection(Object foreignKeyValue,
                                InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionConfiguration<ModelClass, FieldType, RepositoryType> data = internalFormElement.configuration().data();

        VortexCrudDataStore<FieldType, ModelClass> dataStore = configService.configuration().dataStores().get(data.dataStore()).dataStoreInstance();

        java.util.Collection<Object> records = (data.manyToMany() != null) ?
                (java.util.Collection<Object>) manyToManyPersistenceStrategy.resolveManyToMany(dataStore, data.manyToMany(), foreignKeyValue) :
                (java.util.Collection<Object>) data.oneToMany().getData(foreignKeyValue, dataStore, data);

        if (internalFormElement.configuration().data().oneToMany() != null) {
            addOneToManyItems(foreignKeyValue, internalFormElement, formCreator, list, header, records, dataStore);
        } else if (internalFormElement.configuration().data().manyToMany() != null) {
            addManyToManyItems(foreignKeyValue, internalFormElement, formCreator, list, header, records, dataStore);
        } else {
            throw new IllegalArgumentException("No collection found for " + foreignKeyValue);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(internalFormElement.configuration().emptyMessage())));
        }
    }

    private void addManyToManyItems(Object foreignKeyValue,
                                    InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                    FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                                    VerticalLayout list,
                                    HorizontalLayout header,
                                    java.util.Collection<Object> records,
                                    VortexCrudDataStore<FieldType, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(foreignKeyValue, foreignKeyValue, internalFormElement, formCreator, list, header));
            List<FieldType> children = internalFormElement.configuration().data().children();
            children.forEach(fieldId -> item.addContent(new Text(reflectionService.getString(record, fieldId))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(dataStoreUtil.getId(record));
                loadCollection(foreignKeyValue, internalFormElement, formCreator, list, header);
            });
            item.addActions(remove);
            list.add(item);
        }
    }

    private void addOneToManyItems(Object foreignKeyValue,
                                   InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                   FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                                   VerticalLayout list,
                                   HorizontalLayout header,
                                   java.util.Collection<Object> records,
                                   VortexCrudDataStore<FieldType, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(reflectionService.getId(record), foreignKeyValue, internalFormElement, formCreator, list, header));
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> form = internalFormElement.configuration().child().configuration();
            for (InternalFormElement<ModelClass, FieldType, RepositoryType> child : form.children()) {
                String textValue = reflectionService.getString(record, child.field());
                item.addContent(new Text(textValue));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(dataStoreUtil.getId(record));
                    loadCollection(foreignKeyValue, internalFormElement, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private void openDialog(Object entityId,
                            Object foreignKeyValue,
                            InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection<ModelClass, FieldType, RepositoryType> collectionData = internalFormElement.configuration();
        CollectionConfiguration<ModelClass, FieldType, RepositoryType> data = collectionData.data();
        FieldType referenceField = (data.manyToMany() != null) ?
                data.manyToMany().associativeSourceIdField() :
                data.oneToMany().getReferenceField(data);

        com.vaadin.flow.component.dialog.Dialog dialog = internalFormElement.configuration().factoryInstance().create(
                entityId,
                foreignKeyValue,
                referenceField,
                collectionData.child(),
                collectionData.data(),
                collectionData.data().dataStore(),
                () -> loadCollection(foreignKeyValue, internalFormElement, formCreator, list, header),
                () -> {
                },
                formCreator);
        dialog.open();
    }
}