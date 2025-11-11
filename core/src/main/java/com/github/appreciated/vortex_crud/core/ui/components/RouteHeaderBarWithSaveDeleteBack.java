package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.CustomRouteAction;
import com.github.appreciated.vortex_crud.core.config.model.CustomRouteActionContext;
import com.github.appreciated.vortex_crud.core.config.model.CustomRouteActionStyle;
import com.github.appreciated.vortex_crud.core.config.model.CustomRouteActionType;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

public class RouteHeaderBarWithSaveDeleteBack extends HorizontalLayout {

    public RouteHeaderBarWithSaveDeleteBack(boolean isWrapped,
                                            boolean creationMode,
                                            ComponentEventListener<ClickEvent<Button>> onSave,
                                            ComponentEventListener<ClickEvent<Button>> onAdd,
                                            ComponentEventListener<ClickEvent<Button>> onDelete,
                                            ComponentEventListener<ClickEvent<Button>> onBack,
                                            Component titleComponent) {
        this(isWrapped, creationMode, onSave, onAdd, onDelete, onBack, titleComponent, null, null);
    }

    public <ModelClass> RouteHeaderBarWithSaveDeleteBack(boolean isWrapped,
                                            boolean creationMode,
                                            ComponentEventListener<ClickEvent<Button>> onSave,
                                            ComponentEventListener<ClickEvent<Button>> onAdd,
                                            ComponentEventListener<ClickEvent<Button>> onDelete,
                                            ComponentEventListener<ClickEvent<Button>> onBack,
                                            Component titleComponent,
                                            List<CustomRouteAction<ModelClass>> customActions,
                                            CustomRouteActionContext<ModelClass> actionContext) {

        if (!isWrapped && onBack != null) {
            Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), onBack);
            back.getStyle().set("font-size", "1.6em")
                    .set("border-radius", "100%")
                    .set("box-sizing", "content-box");
            add(back);
        }

        // Add the form and buttons to the layout
        add(titleComponent);

        // Add custom action buttons
        if (customActions != null && !customActions.isEmpty() && actionContext != null) {
            for (CustomRouteAction<ModelClass> action : customActions) {
                if (!action.visible()) {
                    continue;
                }

                Button actionButton = createCustomActionButton(action, actionContext);
                add(actionButton);
            }
        }

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

    private <ModelClass> Button createCustomActionButton(CustomRouteAction<ModelClass> action,
                                                          CustomRouteActionContext<ModelClass> context) {
        // Create button with label and optional icon
        Button button = action.icon() != null
                ? new Button(action.label(), action.icon().get())
                : new Button(action.label());

        // Apply style
        applyButtonStyle(button, action.style());

        // Set tooltip if provided
        if (action.tooltip() != null) {
            button.getElement().setAttribute("title", action.tooltip());
        }

        // Set up enablement logic
        updateButtonEnablement(button, action, context);

        // Add click listener
        button.addClickListener(e -> {
            if (action.requiresConfirmation()) {
                // Show confirmation dialog
                context.showConfirmationDialog(
                        action.confirmationTitle() != null ? action.confirmationTitle() : "Confirm Action",
                        action.confirmationMessage() != null ? action.confirmationMessage() : "Are you sure?",
                        () -> executeAction(action, context)
                );
            } else {
                executeAction(action, context);
            }
        });

        return button;
    }

    private void applyButtonStyle(Button button, CustomRouteActionStyle style) {
        switch (style) {
            case PRIMARY:
                button.addThemeVariants(LUMO_PRIMARY);
                break;
            case TERTIARY:
                button.addThemeVariants(LUMO_TERTIARY);
                break;
            case ERROR:
                button.addThemeVariants(LUMO_ERROR);
                break;
            case SUCCESS:
                button.addThemeVariants(LUMO_SUCCESS);
                break;
            case SECONDARY:
            default:
                // Secondary is the default Vaadin button style
                break;
        }
    }

    private <ModelClass> void updateButtonEnablement(Button button,
                                                      CustomRouteAction<ModelClass> action,
                                                      CustomRouteActionContext<ModelClass> context) {
        // Check custom enablement condition first
        if (action.enablementCondition() != null) {
            button.setEnabled(action.enablementCondition().get());
            return;
        }

        // Default enablement based on action type
        Set<ModelClass> selectedEntities = context.getSelectedEntities();
        switch (action.actionType()) {
            case GLOBAL:
                button.setEnabled(true);
                break;
            case SINGLE_ENTITY:
                button.setEnabled(selectedEntities.size() == 1);
                break;
            case MULTI_ENTITY:
                button.setEnabled(!selectedEntities.isEmpty());
                break;
        }
    }

    private <ModelClass> void executeAction(CustomRouteAction<ModelClass> action,
                                             CustomRouteActionContext<ModelClass> context) {
        try {
            action.handler().execute(context);
        } catch (Exception e) {
            context.showErrorNotification("Action failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
