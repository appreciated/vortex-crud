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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectDialogFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
    private final ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtilStrategy;

    public ConnectDialogFactory(
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy,
            ReflectionService<FieldType> reflectionService, VortexCrudDataStoreUtilStrategy dataStoreUtilStrategy) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
        this.reflectionService = reflectionService;
        this.dataStoreUtilStrategy = dataStoreUtilStrategy;
    }

    /**
     * Creates a dialog for managing many-to-many relationships between entities.
     *
     * @param entityId                The ID of the source entity for which connections are being managed. Can be null for new entities.
     * @param foreignKeyValue         The value of the foreign key field in the associative table. Can be null for new entities.
     * @param foreignKeyField         The field in the associative table that references the source entity.
     * @param formRouteRenderer       The renderer configuration for the form route.
     * @param collectionConfiguration Configuration for the collection, including many-to-many relationship details.
     * @param dataStoreKey            The key identifying the target data store.
     * @param routeFactory            Registry for route factories.
     * @param storeListener           Callback to be executed when changes are stored successfully.
     * @param cancelListener          Callback to be executed when the operation is cancelled.
     * @param formCreator             Factory for creating forms.
     * @return A Dialog component allowing users to manage entity connections.
     */
    @Override
    public Dialog create(@Nullable Object entityId,
                         @Nullable Object foreignKeyValue,
                         @Nullable FieldType foreignKeyField,
                         RouteRenderer<ModelClass, FieldType, RepositoryType> formRouteRenderer,
                         CollectionConfiguration<ModelClass, FieldType, RepositoryType> collectionConfiguration,
                         RepositoryType dataStoreKey,
                         VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
                         OnStoreListener storeListener,
                         OnCancelListener cancelListener,
                         FormCreator<ModelClass, FieldType, RepositoryType> formCreator) {

        VortexCrudDataStore<FieldType, ?> dataStore = dataStoreFactoryRegistry.getDataStore(dataStoreKey);
        ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany = collectionConfiguration.getManyToMany();
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        HashMap<String, Object> availableConnections = new HashMap<>(dataStore.getRecordsFromTable(0, Integer.MAX_VALUE)
                .stream()
                .map(o -> (Object) o)
                .collect(Collectors.toMap(dataStoreUtilStrategy::getId, o -> o)));

        List<ModelClass> list = manyToManyPersistenceStrategy.resolveManyToMany(
                dataStore,
                manyToMany,
                entityId
        ).stream().toList();
        Set<Object> previousAssociativeEntries = list.stream()
                .map(dataStoreId -> availableConnections.get(dataStoreUtilStrategy.getId(dataStoreId)))
                .collect(Collectors.toSet());

        // Create a list of selectable items
        MultiSelectListBox<Object> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections.values());
        List<FieldType> children = collectionConfiguration.getChildren();
        connectionList.setItemLabelGenerator(obj -> children.stream()
                .map(fieldId -> {
                    try {
                        Object value = reflectionService.getValue(obj, fieldId);
                        return value != null ? value.toString() : "";
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(","))
        );
        connectionList.setValue(previousAssociativeEntries);

        layout.add(connectionList);

        // Footer buttons
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> {
            cancelListener.onCancel();
            dialog.close();
        });
        Button connectButton = new Button(dialog.getTranslation("button.link.title"), event -> {
            Set<Object> newSelectedConnections = connectionList.getSelectedItems();

            // Find objects to insert (new connections)
            List<Object> toBeInserted = newSelectedConnections.stream()
                    .filter(selection -> !previousAssociativeEntries.contains(selection))
                    .toList();

            // Insert new connections
            if (!toBeInserted.isEmpty()) {
                manyToManyPersistenceStrategy.insert(foreignKeyValue, toBeInserted, manyToMany);
            }

            // Find objects to delete (removed connections)
            List<Object> toBeDeleted = previousAssociativeEntries.stream()
                    .filter(connection -> !newSelectedConnections.contains(connection))
                    .toList();

            // Delete removed connections
            if (!toBeDeleted.isEmpty()) {
                manyToManyPersistenceStrategy.deleteAll(foreignKeyValue, toBeDeleted, manyToMany);
            }

            storeListener.onStore();
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
