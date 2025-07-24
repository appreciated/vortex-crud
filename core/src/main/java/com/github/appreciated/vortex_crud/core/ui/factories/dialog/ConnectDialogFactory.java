package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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

public class ConnectDialogFactory<DataStoreId, FieldId, KeyType> implements VortexCrudDialogFactory<DataStoreId, FieldId, KeyType> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    private final ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> manyToManyPersistenceStrategy;
    private final ReflectionService<FieldId> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public ConnectDialogFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
            ManyToManyPersistenceStrategy<DataStoreId, FieldId, KeyType> manyToManyPersistenceStrategy,
            ReflectionService<FieldId> reflectionService,
            VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                         @Nullable String foreignKeyValue,
                         @Nullable FieldId foreignKeyField,
                         RouteRenderer<DataStoreId, FieldId, KeyType> formRouteRenderer,
                         CollectionConfiguration<DataStoreId, FieldId, KeyType> collectionConfiguration,
                         KeyType dataStoreKey,
                         VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                         OnStoreListener listener,
                         FormCreator<DataStoreId, FieldId, KeyType> formCreator) {

        VortexCrudDataStore<FieldId, ?> dataStore = dataStoreFactoryRegistry.getDataStore(dataStoreKey);
        ManyToMany<DataStoreId, FieldId, KeyType> manyToMany = collectionConfiguration.getManyToMany();
        FieldId associativeTargetIdField = manyToMany.getAssociativeTargetIdField();
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        List<?> availableConnections = dataStore.getRecordsFromTable(0, Integer.MAX_VALUE);

        // Use reflection to call getManyToMany with the correct type
        List<?> currentAssociativeEntries;
        try {
            java.lang.reflect.Method getManyToManyMethod = manyToManyPersistenceStrategy.getClass().getMethod("getManyToMany", 
                VortexCrudDataStore.class, ManyToMany.class, Class.class);
            currentAssociativeEntries = (List<?>) getManyToManyMethod.invoke(manyToManyPersistenceStrategy, 
                dataStore, manyToMany, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call getManyToMany", e);
        }
        Set<String> currentlySelectedConnectionIds = currentAssociativeEntries.stream()
                .map(entity -> reflectionService.getString(entity, associativeTargetIdField)).collect(Collectors.toSet());
        Set<Object> currentlySelectedConnections = availableConnections.stream()
                .filter(Object -> currentlySelectedConnectionIds.contains(dataStoreUtil.getId(Object)))
                .collect(Collectors.toSet());

        // Create a list of selectable items
        MultiSelectListBox<Object> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections);
        connectionList.setItemLabelGenerator(obj -> {
            // Use reflection to get values from the object based on the children configuration
            return collectionConfiguration.getChildren().stream()
                .map(fieldId -> {
                    try {
                        Object value = reflectionService.getValue(obj, fieldId);
                        return value != null ? value.toString() : "";
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(","));
        });
        connectionList.setValue(currentlySelectedConnections);

        layout.add(connectionList);

        // Footer buttons
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button connectButton = new Button(dialog.getTranslation("button.link.title"), event -> {
            Set<Object> newSelectedConnections = connectionList.getSelectedItems();
            // Determine the IDs of the newly selected connections
            Set<String> newSelectedConnectionIds = newSelectedConnections.stream()
                    .filter(Object -> !currentlySelectedConnectionIds.contains(dataStoreUtil.getId(Object)))
                    .map(dataStoreUtil::getId)
                    .collect(Collectors.toSet());

            List<Object> toBeInserted = newSelectedConnectionIds.stream().map(value -> {
                Object newAssociation = new Object();
                reflectionService.setValue(newAssociation, foreignKeyField, foreignKeyValue);
                reflectionService.setValue(newAssociation, associativeTargetIdField, value);
                return newAssociation;
            }).toList();
            // Use reflection to call insert with the correct type
            try {
                java.lang.reflect.Method insertMethod = manyToManyPersistenceStrategy.getClass().getMethod("insert", List.class, Class.class);
                insertMethod.invoke(manyToManyPersistenceStrategy, toBeInserted, Object.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to call insert", e);
            }

            // Remove the connections that are no longer selected
            Set<String> newSelectedIds = newSelectedConnections.stream()
                    .map(dataStoreUtil::getId)
                    .collect(Collectors.toSet());

            // Find all current entries whose target id is **not** present in the new selection, i.e. remove them
            List<?> entriesToDelete = currentAssociativeEntries.stream()
                    .filter(entry -> {
                        Object targetIdObj = reflectionService.getValue(entry, associativeTargetIdField);
                        if (targetIdObj == null) {
                            return false;
                        }
                        return !newSelectedIds.contains(targetIdObj.toString());
                    }).toList();

            // Use reflection to call deleteAll with the correct type
            try {
                java.lang.reflect.Method deleteAllMethod = manyToManyPersistenceStrategy.getClass().getMethod("deleteAll", List.class, Class.class);
                deleteAllMethod.invoke(manyToManyPersistenceStrategy, entriesToDelete, Object.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to call deleteAll", e);
            }

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
