package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
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

public class ListCollectionFactory<DataStoreId, FieldId, ModelClass> implements VortexCrudCollectionFactory<DataStoreId, FieldId, ModelClass> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactory;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ListCollectionFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                                 VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactory,
                                 VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.dialogFactory = dialogFactory;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component createCollection(String foreignKey,
                                      RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer,
                                      InternalFormElement<DataStoreId, FieldId, ModelClass> factoryConfig,
                                      VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory,
                                      FormCreator<DataStoreId, FieldId, ModelClass> formCreator) {
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

    private void loadCollection(String foreignKeyValue,
                                InternalFormElement<DataStoreId, FieldId, ModelClass> internalFormElement,
                                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                                FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                                VerticalLayout list,
                                HorizontalLayout header) {
        list.removeAll();
        list.add(header);
        CollectionConfiguration<DataStoreId, FieldId, ModelClass> data = internalFormElement.getConfiguration().getData();

        VortexCrudDataStore<FieldId, ModelClass> dataStore = dataStoreFactoryRegistry.getDataStore(data.getDataStore());
        List<GenericEntity> records = (data.getManyToMany() != null) ?
                data.getManyToMany().getData(dataStoreFactoryRegistry, foreignKeyValue, dataStore, data) :
                data.getOneToMany().getData(foreignKeyValue, dataStore, data);

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

    private void addManyToManyItems(String foreignKeyValue,
                                    InternalFormElement<DataStoreId, FieldId, ModelClass> internalFormElement,
                                    VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                                    FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                                    VerticalLayout list,
                                    HorizontalLayout header,
                                    List<ModelClass> records,
                                    VortexCrudDataStore<FieldId, ModelClass> dataStore) {
        for (ModelClass record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header));
            List<String> children = internalFormElement.getConfiguration().getData().getChildren();
            children.forEach(s -> item.addContent(new Text(record.getString(s))));
            Button remove = new Button(VaadinIcon.TRASH.create());
            remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
            remove.addClickListener(event -> {
                dataStore.deleteRecordById(DataStoreUtil.getId(record));
                loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header);
            });
            list.add(item);
        }
    }

    private void addOneToManyItems(String foreignKeyValue,
                                   InternalFormElement<DataStoreId, FieldId, ModelClass> internalFormElement,
                                   VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                                   FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                                   VerticalLayout list,
                                   HorizontalLayout header,
                                   List<ModelClass> records,
                                   VortexCrudDataStore<FieldId, ModelClass> dataStore) {
        for (ModelClass record : records) {
            DefaultCollectionItem item = new DefaultCollectionItem();
            item.getContent().addClickListener(event -> openDialog(DataStoreUtil.getId(record), foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header));
            RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> form = internalFormElement.getConfiguration().getChild().getConfiguration();
            for (InternalFormElement<DataStoreId, FieldId, ModelClass> child : form.getChildren()) {
                Object o = record.get(fieldNameResolver.getKeyForFieldId(child.getField()));
                item.addContent(new Text(o.toString()));
                Button remove = new Button(VaadinIcon.TRASH.create());
                remove.addThemeVariants(LUMO_TERTIARY_INLINE, LUMO_SMALL, LUMO_ERROR);
                remove.addClickListener(event -> {
                    dataStore.deleteRecordById(DataStoreUtil.getId(record));
                    loadCollection(foreignKeyValue, internalFormElement, routeFactoryRegistry, formCreator, list, header);
                });
                item.addActions(remove);
            }
            list.add(item);
        }
    }

    private void openDialog(String entityId,
                            String foreignKey,
                            InternalFormElement<DataStoreId, FieldId, ModelClass> internalFormElement,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                            FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                            VerticalLayout list,
                            HorizontalLayout header) {
        Collection<DataStoreId, FieldId, ModelClass> collectionData = internalFormElement.getConfiguration();
        CollectionConfiguration<DataStoreId, FieldId, ModelClass> data = collectionData.getData();
        FieldId referenceField = (data.getManyToMany() != null) ?
                data.getManyToMany().getAssociativeSourceIdField() :
                data.getOneToMany().getReferenceField(data);

        com.vaadin.flow.component.dialog.Dialog dialog = dialogFactory.getFactory(internalFormElement.getConfiguration().getFactory()).create(
                entityId,
                foreignKey,
                referenceField,
                collectionData.getChild(),
                collectionData.getData(),
                collectionData.getData().getDataStore(),
                routeFactoryRegistry,
                () -> loadCollection(foreignKey, internalFormElement, routeFactoryRegistry, formCreator, list, header),
                formCreator);
        dialog.open();
    }
}