package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.config.model.CollectionData;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectDialogFactory<DataStoreId, FieldId> implements VortexCrudDialogFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;

    public ConnectDialogFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
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
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        List<GenericEntity> availableConnections = dataStore.getRecordsFromTable(0, Integer.MAX_VALUE);

        // Create a list of selectable items
        MultiSelectListBox<GenericEntity> connectionList = new MultiSelectListBox<>();
        connectionList.setItems(availableConnections);
        connectionList.setItemLabelGenerator(genericEntity -> collectionData.getChildren().stream().map(genericEntity::getString).collect(Collectors.joining(",")));

        layout.add(connectionList);

        // Footer buttons
        Button cancelButton = new Button(dialog.getTranslation("button.cancel.title"), event -> dialog.close());
        Button connectButton = new Button(dialog.getTranslation("button.link.title"), event -> {
            Set<GenericEntity> selectedConnections = connectionList.getSelectedItems();
            if (!selectedConnections.isEmpty()) {
                // Process selected connections
                listener.onStore();
                dialog.close();
            } else {
                Notification notification = Notification.show(dialog.getTranslation("dialog.notification.no-selection"));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
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
