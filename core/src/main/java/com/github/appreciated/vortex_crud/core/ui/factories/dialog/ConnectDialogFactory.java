package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectDialogFactory<DataStoreId, FieldId, ModelClass>  implements VortexCrudDialogFactory<DataStoreId, FieldId, ModelClass>  {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass>  dataStoreFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ManyToManyPersistenceStrategy<DataStoreId, FieldId, ModelClass>  manyToManyPersistenceStrategy;

    public ConnectDialogFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass>  dataStoreFactoryRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
            ManyToManyPersistenceStrategy<DataStoreId, FieldId, ModelClass>  manyToManyPersistenceStrategy) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                         @Nullable String foreignKeyValue,
                         @Nullable FieldId foreignKeyField,
                         RouteRenderer<DataStoreId, FieldId, ModelClass>  formRouteRenderer,
                         CollectionConfiguration<DataStoreId, FieldId, ModelClass>  collectionConfiguration,
                         DataStoreId dataStoreIdentifier,
                         VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass>  routeFactory,
                         OnStoreListener listener,
                         FormCreator<DataStoreId, FieldId, ModelClass>  formCreator) {

        VortexCrudDataStore<FieldId, ModelClass> dataStore = dataStoreFactoryRegistry.getDataStore(dataStoreIdentifier);
        ManyToMany<DataStoreId, FieldId, ModelClass>  manyToMany = collectionConfiguration.getManyToMany();
        FieldId associativeTargetIdField = manyToMany.getAssociativeTargetIdField();
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        List<ModelClass> availableConnections = dataStore.getRecordsFromTable(0, Integer.MAX_VALUE);

        List<ModelClass> currentAssociativeEntries = manyToManyPersistenceStrategy.getManyToMany(dataStore, manyToMany);
        Set<String> currentlySelectedConnectionIds = currentAssociativeEntries.stream()
                .map(genericEntity -> genericEntity.get(fieldNameResolver.getKeyForFieldId(associativeTargetIdField)).toString()).collect(Collectors.toSet());
        Set<ModelClass> currentlySelectedConnections = availableConnections.stream()
                .filter(genericEntity -> currentlySelectedConnectionIds.contains(DataStoreUtil.getId(genericEntity)))
                .collect(Collectors.toSet());

        // Create a list of selectable items
        MultiSelectListBox<ModelClass> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections);
        connectionList.setItemLabelGenerator(genericEntity -> collectionConfiguration.getChildren().stream().map(genericEntity::getString).collect(Collectors.joining(",")));
        connectionList.setValue(currentlySelectedConnections);

        layout.add(connectionList);

        // Footer buttons
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button connectButton = new Button(dialog.getTranslation("button.link.title"), event -> {
            Set<ModelClass> newSelectedConnections = connectionList.getSelectedItems();
            // Determine the IDs of the newly selected connections
            Set<String> newSelectedConnectionIds = newSelectedConnections.stream()
                    .filter(genericEntity -> !currentlySelectedConnectionIds.contains(DataStoreUtil.getId(genericEntity)))
                    .map(DataStoreUtil::getId)
                    .collect(Collectors.toSet());

            List<ModelClass> toBeInserted = newSelectedConnectionIds.stream().map(value -> {
                ModelClass newAssociation = dataStore.createModelInstance();
                newAssociation.put(fieldNameResolver.getKeyForFieldId(foreignKeyField), foreignKeyValue);
                newAssociation.put(fieldNameResolver.getKeyForFieldId(associativeTargetIdField), value);
                return newAssociation;
            }).toList();
            manyToManyPersistenceStrategy.insert(toBeInserted);

            // Remove the connections that are no longer selected
            Set<String> newSelectedIds = newSelectedConnections.stream()
                    .map(DataStoreUtil::getId)
                    .collect(Collectors.toSet());

            // Find all current entries whose target id is **not** present in the new selection, i.e. remove them
            List<ModelClass> toBeDeleted = currentAssociativeEntries.stream()
                    .filter(entry -> {
                        Object targetIdObj = entry.get(fieldNameResolver.getKeyForFieldId(associativeTargetIdField));
                        if (targetIdObj == null) {
                            return false;
                        }
                        return !newSelectedIds.contains(targetIdObj.toString());
                    }).toList();
            manyToManyPersistenceStrategy.deleteAll(toBeDeleted);

            listener.onStore();
            dialog.close();
        });
        connectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(layout);
        dialog.getFooter().add(cancelButton, connectButton);

        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.setMinWidth(500, Unit.PIXELS);
        return dialog;
    }
}
