package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionData;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectDialogFactory<DataStoreId, FieldId> implements VortexCrudDialogFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ConnectDialogFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry, VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                             @Nullable String foreignKeyValue,
                             @Nullable FieldId foreignKeyField,
                             RouteRenderer<DataStoreId, FieldId> formRouteRenderer,
                             CollectionData<DataStoreId, FieldId> collectionData,
                             DataStoreId dataStoreIdentifier,
                             VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                             OnStoreListener listener,
                             FormCreator<DataStoreId, FieldId> formCreator) {

        VortexCrudDataStore<FieldId> dataStore = dataStoreFactoryRegistry.getFactory(dataStoreIdentifier);
        VortexCrudDataStore<FieldId> associativeDatastore = dataStoreFactoryRegistry.getFactory(collectionData.getManyToMany().getAssociativeDataStore());
        FieldId associativeTargetIdField = collectionData.getManyToMany().getAssociativeTargetIdField();
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        List<GenericEntity> availableConnections = dataStore.getRecordsFromTable(0, Integer.MAX_VALUE);

        List<GenericEntity> currentAssociativeEntries = associativeDatastore.getRecordsFromTableWhereColumnEquals(foreignKeyField, foreignKeyValue, 0, Integer.MAX_VALUE).stream().toList();
        Set<String> currentlySelectedConnectionIds = currentAssociativeEntries.stream()
                .map(record -> record.getString(fieldNameResolver.getKeyForFieldId(associativeTargetIdField))).collect(Collectors.toSet());
        Set<GenericEntity> currentlySelectedConnections = availableConnections.stream().filter(genericEntity -> currentlySelectedConnectionIds.contains(DataStoreUtil.getId(genericEntity))).collect(Collectors.toSet());

        // Create a list of selectable items
        MultiSelectListBox<GenericEntity> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections);
        connectionList.setItemLabelGenerator(genericEntity -> collectionData.getChildren().stream().map(genericEntity::getString).collect(Collectors.joining(",")));
        connectionList.setValue(currentlySelectedConnections);

        layout.add(connectionList);

        // Footer buttons
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button connectButton = new Button(dialog.getTranslation("button.link.title"), event -> {
            Set<GenericEntity> newSelectedConnections = connectionList.getSelectedItems();
            // Determine the IDs of the newly selected connections
            Set<String> newSelectedConnectionIds = newSelectedConnections.stream()
                    .filter(genericEntity -> !currentlySelectedConnectionIds.contains(DataStoreUtil.getId(genericEntity)))
                    .map(DataStoreUtil::getId)
                    .collect(Collectors.toSet());

            newSelectedConnectionIds.forEach(associativeTargetIdFieldValue -> {
                GenericEntity newAssociation = new GenericEntity();
                newAssociation.put(fieldNameResolver.getKeyForFieldId(foreignKeyField), foreignKeyValue);
                newAssociation.put(fieldNameResolver.getKeyForFieldId(associativeTargetIdField), associativeTargetIdFieldValue);
                associativeDatastore.insertRecord(newAssociation);
            });

            // Remove the connections that are no longer selected
            Set<GenericEntity> idsToRemove = currentlySelectedConnections.stream()
                    .filter(o -> !newSelectedConnections.contains(o))
                    .map(genericEntity -> currentAssociativeEntries.stream()
                            .filter(genericEntity1 -> Objects.equals(DataStoreUtil.getId(genericEntity), genericEntity1.getString(fieldNameResolver.getKeyForFieldId(associativeTargetIdField))))
                            .findFirst()
                            .orElseThrow())
                    .collect(Collectors.toSet());
            idsToRemove.forEach(record -> associativeDatastore.deleteRecordById(DataStoreUtil.getId(record)));

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
