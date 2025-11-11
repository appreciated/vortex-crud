package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Context object passed to custom route action handlers.
 * Provides access to the data store, selected entities, and UI utilities.
 *
 * @param <ModelClass> The type of entity
 */
@Getter
@Builder
public class CustomRouteActionContext<ModelClass> implements Serializable {

    /**
     * The data store for performing CRUD operations on entities.
     */
    private final VortexCrudDataStore<?, ModelClass> dataStore;

    /**
     * The currently selected entities (may be empty for GLOBAL actions).
     */
    private final Set<ModelClass> selectedEntities;

    /**
     * Callback to refresh the view after the action is executed.
     * This should be called if the action modifies data that needs to be reflected in the UI.
     */
    private final Runnable refreshCallback;

    /**
     * The root component of the current view, useful for showing dialogs or notifications.
     */
    private final Component viewComponent;

    /**
     * Shows a success notification with the given message.
     *
     * @param message The message to display
     */
    public void showSuccessNotification(String message) {
        Notification notification = Notification.show(message, 3000, Notification.Position.BOTTOM_START);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Shows an error notification with the given message.
     *
     * @param message The message to display
     */
    public void showErrorNotification(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.BOTTOM_START);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Shows an info notification with the given message.
     *
     * @param message The message to display
     */
    public void showInfoNotification(String message) {
        Notification.show(message, 3000, Notification.Position.BOTTOM_START);
    }

    /**
     * Shows a custom dialog.
     *
     * @param dialog The dialog to show
     */
    public void showDialog(Dialog dialog) {
        dialog.open();
    }

    /**
     * Shows a confirmation dialog with Yes/No buttons.
     *
     * @param title The dialog title
     * @param message The confirmation message
     * @param onConfirm Callback executed when user clicks Yes
     */
    public void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);

        com.vaadin.flow.component.html.Div content = new com.vaadin.flow.component.html.Div();
        content.setText(message);
        content.getStyle().set("padding", "var(--lumo-space-m)");
        dialog.add(content);

        com.vaadin.flow.component.button.Button cancelButton = new com.vaadin.flow.component.button.Button("Cancel", e -> dialog.close());
        com.vaadin.flow.component.button.Button confirmButton = new com.vaadin.flow.component.button.Button("Yes", e -> {
            onConfirm.run();
            dialog.close();
        });
        confirmButton.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY);

        dialog.getFooter().add(cancelButton, confirmButton);
        dialog.open();
    }

    /**
     * Gets the first selected entity (useful for SINGLE_ENTITY actions).
     *
     * @return The first selected entity, or null if none selected
     */
    public ModelClass getFirstSelectedEntity() {
        return selectedEntities.isEmpty() ? null : selectedEntities.iterator().next();
    }

    /**
     * Executes an action and handles common error scenarios.
     *
     * @param action The action to execute
     * @param successMessage Message to show on success
     * @param errorMessage Message to show on error
     */
    public void executeWithNotification(Consumer<CustomRouteActionContext<ModelClass>> action,
                                       String successMessage,
                                       String errorMessage) {
        try {
            action.accept(this);
            if (successMessage != null) {
                showSuccessNotification(successMessage);
            }
            if (refreshCallback != null) {
                refreshCallback.run();
            }
        } catch (Exception e) {
            if (errorMessage != null) {
                showErrorNotification(errorMessage + ": " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
}
