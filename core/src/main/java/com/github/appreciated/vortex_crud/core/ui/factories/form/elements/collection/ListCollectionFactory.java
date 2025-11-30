package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
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

    @Override
    public Component createCollection(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                      Object foreignKey,
                                      RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                                      InternalFormElement<ModelClass, FieldType, RepositoryType> factoryConfig) {
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
        button.addClickListener(event -> openDialog(context, null, foreignKey, factoryConfig, list, header));
        header.add(button);
        loadCollection(context, foreignKey, factoryConfig, list, header);
        return list;
    }

    private void loadCollection(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                Object foreignKeyValue,
                                InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionConfiguration<ModelClass, FieldType, RepositoryType> data = internalFormElement.configuration().data();

        VortexCrudDataStore<FieldType, ModelClass> dataStore = data.dataStoreInstance();

        java.util.Collection<Object> records = (data.manyToMany() != null) ?
                (java.util.Collection<Object>) context.getManyToManyPersistenceStrategy().resolveManyToMany(dataStore, data.manyToMany(), foreignKeyValue) :
                (java.util.Collection<Object>) data.oneToMany().getData(foreignKeyValue, dataStore, data);

        if (internalFormElement.configuration().data().oneToMany() != null) {
            addOneToManyItems(context, foreignKeyValue, internalFormElement, list, header, records, dataStore);
        } else if (internalFormElement.configuration().data().manyToMany() != null) {
            addManyToManyItems(context, foreignKeyValue, internalFormElement, list, header, records, dataStore);
        } else {
            throw new IllegalArgumentException("No collection found for " + foreignKeyValue);
        }
        if (records.isEmpty()) {
            list.add(new Text(list.getTranslation(internalFormElement.configuration().emptyMessage())));
        }
    }

    private void addManyToManyItems(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                    Object foreignKeyValue,
                                    InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                    VerticalLayout list,
                                    HorizontalLayout header,
                                    java.util.Collection<Object> records,
                                    VortexCrudDataStore<FieldType, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(context, foreignKeyValue, foreignKeyValue, internalFormElement, list, header));
            List<FieldType> children = internalFormElement.configuration().data().children();
            children.forEach(fieldId -> item.addContent(new Text(context.getReflectionService().getString(record, fieldId))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(context.getDataStoreUtil().getId(record));
                loadCollection(context, foreignKeyValue, internalFormElement, list, header);
            });
            item.addActions(remove);
            list.add(item);
        }
    }

    private void addOneToManyItems(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                                   Object foreignKeyValue,
                                   InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                                   VerticalLayout list,
                                   HorizontalLayout header,
                                   java.util.Collection<Object> records,
                                   VortexCrudDataStore<FieldType, ?> dataStore) {
        for (Object record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(context, context.getReflectionService().getId(record), foreignKeyValue, internalFormElement, list, header));
            RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> form = internalFormElement.configuration().child().configuration();
            for (InternalFormElement<ModelClass, FieldType, RepositoryType> child : form.children()) {
                String textValue = context.getReflectionService().getString(record, child.field());
                item.addContent(new Text(textValue));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(context.getDataStoreUtil().getId(record));
                    loadCollection(context, foreignKeyValue, internalFormElement, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private void openDialog(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                            Object entityId,
                            Object foreignKeyValue,
                            InternalFormElement<ModelClass, FieldType, RepositoryType> internalFormElement,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection<ModelClass, FieldType, RepositoryType> collectionData = internalFormElement.configuration();
        CollectionConfiguration<ModelClass, FieldType, RepositoryType> data = collectionData.data();
        FieldType referenceField = (data.manyToMany() != null) ?
                data.manyToMany().associativeSourceIdField() :
                data.oneToMany().getReferenceField(data);

        com.vaadin.flow.component.dialog.Dialog dialog = context.getDialogFactoryRegistry().getFactory(internalFormElement.configuration().factory()).create(
                context,
                entityId,
                foreignKeyValue,
                referenceField,
                collectionData.child(),
                collectionData.data(),
                collectionData.data().dataStoreInstance(),
                () -> loadCollection(context, foreignKeyValue, internalFormElement, list, header),
                () -> {
                });
        dialog.open();
    }
}
