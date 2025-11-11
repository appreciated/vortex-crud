package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Configuration model for a custom route action button.
 * Custom actions allow you to add interactive buttons to your routes that can:
 * - Operate on selected entities
 * - Perform global operations
 * - Access the repository for data operations
 * - Show dialogs for user input
 * - Display notifications
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CustomRouteAction.<User>builder()
 *     .label("Approve")
 *     .icon(() -> VaadinIcon.CHECK.create())
 *     .actionType(CustomRouteActionType.SINGLE_ENTITY)
 *     .style(CustomRouteActionStyle.SUCCESS)
 *     .handler(context -> {
 *         User user = context.getFirstSelectedEntity();
 *         user.setStatus("APPROVED");
 *         context.getDataStore().updateRecordById(user);
 *         context.showSuccessNotification("User approved!");
 *         context.getRefreshCallback().run();
 *     })
 *     .build()
 * }</pre>
 *
 * @param <ModelClass> The type of entity
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomRouteAction<ModelClass> implements Serializable {

    /**
     * The label/text displayed on the button.
     */
    private String label;

    /**
     * Optional icon supplier for the button.
     * Example: () -> VaadinIcon.CHECK.create()
     */
    private SerializableSupplier<Icon> icon;

    /**
     * The type of action (GLOBAL, SINGLE_ENTITY, MULTI_ENTITY).
     * Determines when the button is enabled based on selection.
     */
    @Builder.Default
    private CustomRouteActionType actionType = CustomRouteActionType.GLOBAL;

    /**
     * The visual style of the button (PRIMARY, SECONDARY, TERTIARY, ERROR, SUCCESS).
     */
    @Builder.Default
    private CustomRouteActionStyle style = CustomRouteActionStyle.SECONDARY;

    /**
     * The handler that executes when the button is clicked.
     * Receives a context with access to the data store, selected entities, and UI utilities.
     */
    private CustomRouteActionHandler<ModelClass> handler;

    /**
     * Optional tooltip text displayed when hovering over the button.
     */
    private String tooltip;

    /**
     * Whether the button should be visible.
     * Can be used for conditional visibility based on permissions or other criteria.
     */
    @Builder.Default
    private boolean visible = true;

    /**
     * Whether to show a confirmation dialog before executing the action.
     * If true, confirmationTitle and confirmationMessage should be provided.
     */
    @Builder.Default
    private boolean requiresConfirmation = false;

    /**
     * The title of the confirmation dialog (if requiresConfirmation is true).
     */
    private String confirmationTitle;

    /**
     * The message of the confirmation dialog (if requiresConfirmation is true).
     */
    private String confirmationMessage;

    /**
     * Optional: Custom enablement condition.
     * If provided, this function determines whether the button is enabled.
     * Useful for complex business logic beyond the standard action type rules.
     */
    private SerializableSupplier<Boolean> enablementCondition;
}
