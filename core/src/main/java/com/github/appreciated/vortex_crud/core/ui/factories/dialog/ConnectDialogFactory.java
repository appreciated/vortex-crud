package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyRelation;
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
import java.util.stream.Stream;

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
    public Dialog create(@Nullable String entityId,
                         @Nullable String foreignKeyValue,
                         @Nullable FieldId foreignKeyField,
                         RouteRenderer<DataStoreId, FieldId, KeyType> formRouteRenderer,
                         CollectionConfiguration<DataStoreId, FieldId, KeyType> collectionConfiguration,
                         KeyType dataStoreKey,
                         VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                         OnStoreListener storeListener,
                         OnCancelListener cancelListener,
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
        List<Object> availableConnections = dataStore.getRecordsFromTable(0, Integer.MAX_VALUE)
                .stream()
                .map(o -> (Object) o)
                .toList();

        Set<Object> previousAssociativeEntries = manyToManyPersistenceStrategy.resolveManyToMany(
                        dataStore,
                        manyToMany,
                        entityId
                ).stream()
                .map(dataStoreId -> (Object) dataStoreId)
                .collect(Collectors.toSet());

        // Create a list of selectable items
        MultiSelectListBox<Object> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections);
        List<FieldId> children = collectionConfiguration.getChildren();
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
            // Determine the IDs of the newly selected connections
            Set<String> newSelectedConnectionIds = newSelectedConnections.stream()
                    .filter(selection -> !previousAssociativeEntries.contains(selection))
                    .map(dataStoreUtil::getId)
                    .collect(Collectors.toSet());

            List<ManyToManyRelation> toBeInserted = newSelectedConnectionIds.stream()
                    .map(value -> new ManyToManyRelation(foreignKeyValue, value))
                    .toList();
            // Use reflection to call insert with the correct type
            manyToManyPersistenceStrategy.insert(toBeInserted, manyToMany);

            List<ManyToManyRelation> toBeDeleted = previousAssociativeEntries.stream()
                    .filter(connection -> !newSelectedConnections.contains(connection))
                    .map(entry -> reflectionService.getValue(entry, associativeTargetIdField))
                    .map(o -> new ManyToManyRelation(foreignKeyValue, o))
                    .toList();

            // Use reflection to call deleteAll with the correct type
            manyToManyPersistenceStrategy.deleteAll(toBeDeleted, manyToMany);

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
