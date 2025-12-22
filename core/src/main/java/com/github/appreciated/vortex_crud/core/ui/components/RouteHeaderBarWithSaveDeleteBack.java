package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

/**
 * Header bar component for routes with standard buttons (save, delete, back)
 * and support for custom route actions.
 */
public class RouteHeaderBarWithSaveDeleteBack extends HorizontalLayout {

    private final List<ActionComponentBinding<?, ?>> actionBindings = new ArrayList<>();

    public RouteHeaderBarWithSaveDeleteBack(boolean isWrapped,
                                            boolean creationMode,
                                            ComponentEventListener<ClickEvent<Button>> onSave,
                                            ComponentEventListener<ClickEvent<Button>> onAdd,
                                            ComponentEventListener<ClickEvent<Button>> onDelete,
                                            ComponentEventListener<ClickEvent<Button>> onBack,
                                            Component titleComponent) {

        if (!isWrapped && onBack != null) {
            Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), onBack);
            back.getStyle().set("font-size", "1.6em")
                    .set("border-radius", "100%")
                    .set("box-sizing", "content-box");
            add(back);
        }

        // Add the form and buttons to the layout
        add(titleComponent);
        if (onAdd != null) {
            Button add = new Button(getTranslation("button.create.title"), onAdd);
            add.addThemeVariants(LUMO_PRIMARY);
            add(add);
        }

        if (onSave != null) {
            Button save = new Button(getTranslation("button.save.title"), onSave);
            save.addThemeVariants(LUMO_PRIMARY);
            add(save);
        }

        if (!creationMode && onDelete != null) {
            // Generic Delete button
            Button delete = new Button(getTranslation("button.delete.title"), onDelete);
            delete.addThemeVariants(LUMO_ERROR);
            add(delete);
        }
        setAlignItems(CENTER);
        setMinHeight("53px");
        getStyle().set("box-sizing", "content-box");
    }

    /**
     * Renders custom route actions and registers their handlers.
     * This method should be called after the standard buttons are added.
     *
     * @param actions The list of route actions to render
     * @param contextProvider Provider for the current action context (allows dynamic updates)
     * @param <FieldType> The type used to identify fields
     * @param <ModelClass> The type of entity
     */
    public <FieldType, ModelClass> void renderActions(
            List<RouteAction<FieldType, ModelClass>> actions,
            Consumer<Consumer<RouteActionContext<FieldType, ModelClass>>> contextProvider) {

        if (actions == null || actions.isEmpty()) {
            return;
        }

        for (RouteAction<FieldType, ModelClass> action : actions) {
            if (!action.visible()) {
                continue;
            }

            // Create the component using the factory
            Component component = action.componentFactory().get();

            // Register click listener if component supports clicking
            if (component instanceof ClickNotifier) {
                ((ClickNotifier<?>) component).addClickListener(event -> {
                    contextProvider.accept(context -> action.handle(context));
                });
            }

            // Store binding for enablement updates
            ActionComponentBinding<FieldType, ModelClass> binding =
                    new ActionComponentBinding<>(action, component);
            actionBindings.add(binding);

            // Set initial enabled state
            contextProvider.accept(context -> updateComponentEnabledState(binding, context));

            // Add to layout
            add(component);
        }
    }

    /**
     * Updates the enabled state of all action components based on current context.
     * Call this method when the selection state changes.
     *
     * @param context The current action context
     * @param <FieldType> The type used to identify fields
     * @param <ModelClass> The type of entity
     */
    public <FieldType, ModelClass> void updateActionStates(RouteActionContext<FieldType, ModelClass> context) {
        for (ActionComponentBinding<?, ?> binding : actionBindings) {
            @SuppressWarnings("unchecked")
            ActionComponentBinding<FieldType, ModelClass> typedBinding =
                    (ActionComponentBinding<FieldType, ModelClass>) binding;
            updateComponentEnabledState(typedBinding, context);
        }
    }

    private <FieldType, ModelClass> void updateComponentEnabledState(
            ActionComponentBinding<FieldType, ModelClass> binding,
            RouteActionContext<FieldType, ModelClass> context) {

        if (binding.component instanceof HasEnabled) {
            boolean enabled = binding.action.isEnabled(context);
            ((HasEnabled) binding.component).setEnabled(enabled);
        }
    }

    /**
     * Internal class to bind actions to their components for state management
     */
    private static class ActionComponentBinding<FieldType, ModelClass> {
        final RouteAction<FieldType, ModelClass> action;
        final Component component;

        ActionComponentBinding(RouteAction<FieldType, ModelClass> action, Component component) {
            this.action = action;
            this.component = component;
        }
    }
}
