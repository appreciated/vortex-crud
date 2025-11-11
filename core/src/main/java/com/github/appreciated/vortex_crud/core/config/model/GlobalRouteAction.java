package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A global route action that doesn't require any entity selection.
 * Global actions are always enabled and can perform operations that don't depend on selected entities.
 *
 * <p>Examples: Export All, Generate Report, Import Data, Refresh, Settings</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * GlobalRouteAction.<Task>builder()
 *     .componentFactory(context -> {
 *         Button exportBtn = new Button("Export All", VaadinIcon.DOWNLOAD.create());
 *         exportBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
 *         exportBtn.addClickListener(e -> {
 *             List<Task> allTasks = context.getDataStore()
 *                 .getRecordsFromTable(0, Integer.MAX_VALUE);
 *             exportToCSV(allTasks);
 *             context.showSuccessNotification("Exported " + allTasks.size() + " tasks");
 *         });
 *         return exportBtn;
 *     })
 *     .visible(true)
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
public class GlobalRouteAction<ModelClass> implements RouteAction<ModelClass> {

    /**
     * Factory that creates the component for this action.
     * The factory receives the action context and should return a component with click listeners registered.
     */
    private SerializableFunction<CustomRouteActionContext<ModelClass>, Component> componentFactory;

    /**
     * Whether this action should be visible.
     */
    @Builder.Default
    private boolean visible = true;

    @Override
    public SerializableSupplier<Component> componentFactory(CustomRouteActionContext<ModelClass> context) {
        return () -> componentFactory.apply(context);
    }
}
