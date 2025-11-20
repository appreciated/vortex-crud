package com.github.appreciated.vortex_crud.core.ui.actions;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Context object provided to route actions when they are executed.
 * Contains access to the data store, selected entities, and utility methods.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * SingleEntityRouteAction.<Task>builder()
 *     .componentFactory(() -> new Button("Approve"))
 *     .handler(context -> {
 *         Task task = context.getFirstSelectedEntity();
 *         task.setStatus("APPROVED");
 *         context.getDataStore().updateRecord(task);
 *         context.showSuccessNotification("Task approved!");
 *         context.getRefreshCallback().run();
 *     })
 *     .build()
 * }</pre>
 *
 * @param <FieldType> The type used to identify fields in the data store
 * @param <ModelClass> The type of entity
 */
@Accessors(fluent = true)
@Builder
@Getter
public class RouteActionContext<FieldType, ModelClass> implements Serializable {

    /**
     * The data store for performing CRUD operations
     */
    private final VortexCrudDataStore<FieldType, ModelClass> dataStore;

    /**
     * The currently selected entities (may be empty)
     */
    private final List<ModelClass> selectedEntities;

    /**
     * Callback to refresh the view after actions are performed
     */
    private final Runnable refreshCallback;

    /**
     * Reference to the view component (for advanced use cases)
     */
    private final Component viewComponent;

    /**
     * Gets the first selected entity.
     * Useful for single-entity actions.
     *
     * @return The first selected entity, or null if no entities are selected
     */
    public ModelClass getFirstSelectedEntity() {
        return selectedEntities != null && !selectedEntities.isEmpty()
            ? selectedEntities.get(0)
            : null;
    }

    /**
     * Gets the number of selected entities.
     *
     * @return The count of selected entities
     */
    public int getSelectionCount() {
        return selectedEntities != null ? selectedEntities.size() : 0;
    }

    /**
     * Shows a success notification with the given message.
     *
     * @param message The message to display
     */
    public void showSuccessNotification(String message) {
        Notification notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    /**
     * Shows an error notification with the given message.
     *
     * @param message The message to display
     */
    public void showErrorNotification(String message) {
        Notification notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    /**
     * Shows an info notification with the given message.
     *
     * @param message The message to display
     */
    public void showInfoNotification(String message) {
        Notification notification = Notification.show(message);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    /**
     * Shows a warning notification with the given message.
     *
     * @param message The message to display
     */
    public void showWarningNotification(String message) {
        Notification notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }
}
