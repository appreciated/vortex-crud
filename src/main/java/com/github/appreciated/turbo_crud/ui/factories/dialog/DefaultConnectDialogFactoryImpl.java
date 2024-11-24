package com.github.appreciated.turbo_crud.ui.factories.dialog;

import com.github.appreciated.turbo_crud.config.model.CollectionData;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
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

public class DefaultConnectDialogFactoryImpl implements TurboCrudDialogFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;

    public DefaultConnectDialogFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
    }

    @Override
    public Dialog create(@Nullable String entityId,
                         @Nullable String foreignKeyValue,
                         @Nullable String foreignKeyField,
                         Route formRoute,
                         CollectionData collectionData,
                         String repository,
                         TurboCrudRouteFactoryRegistry routeFactory,
                         OnStoreListener listener,
                         FormCreator formCreator) {

        TurboCrudEntityManager entityManager = entityManagerFactoryRegistry.getFactory(repository);
        Dialog dialog = new Dialog();
        dialog.setMaxWidth("1200px");
        dialog.setHeaderTitle(dialog.getTranslation("button.link.title"));

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);

        // Fetch available connections
        List<GenericEntity> availableConnections = entityManager.getRecordsFromTable(0, Integer.MAX_VALUE);

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
