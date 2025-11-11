package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.CustomRouteActionContext;
import com.github.appreciated.vortex_crud.core.config.model.RouteAction;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
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
                                            List<RouteAction<ModelClass>> routeActions,
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

        // Add custom action components
        if (routeActions != null && !routeActions.isEmpty() && actionContext != null) {
            for (RouteAction<ModelClass> action : routeActions) {
                if (!action.visible()) {
                    continue;
                }

                // Create the component from the factory
                Component actionComponent = action.componentFactory().get();

                // Register click listener if the component supports it
                if (actionComponent instanceof ClickNotifier) {
                    ((ClickNotifier<?>) actionComponent).addClickListener(e -> {
                        try {
                            action.handle(actionContext);
                        } catch (Exception ex) {
                            actionContext.showErrorNotification("Action failed: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    });
                }

                // Set enablement based on action's isEnabled method
                if (actionComponent instanceof HasEnabled) {
                    ((HasEnabled) actionComponent).setEnabled(action.isEnabled(actionContext));
                }

                add(actionComponent);
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
}
