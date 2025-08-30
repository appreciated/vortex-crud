package com.github.appreciated.vortex_crud.core.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

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

        if (!isWrapped && onBack != null) {
            Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), onBack);
            back.getStyle().set("font-size", "1.6em")
                    .set("padding", "calc(var(--lumo-button-size) / 8)")
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
}
