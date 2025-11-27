package com.github.appreciated.vortex_crud.security.core.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * View displayed when a user attempts to access a resource they don't have permission for.
 * Provides a user-friendly message and navigation options.
 * Can be used both as a regular route and as an error handler.
 */
@Route("access-denied")
@PageTitle("Access Denied")
@AnonymousAllowed
public class AccessDeniedView extends VerticalLayout implements BeforeEnterObserver {

    private final Paragraph messageDetails;

    public AccessDeniedView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle()
                .set("background", "var(--lumo-contrast-5pct)")
                .set("padding", "var(--lumo-space-l)");

        // Error icon
        Icon errorIcon = VaadinIcon.BAN.create();
        errorIcon.setSize("80px");
        errorIcon.getStyle()
                .set("color", "var(--lumo-error-color)")
                .set("margin-bottom", "var(--lumo-space-m)");

        // Title
        H1 title = new H1("Access Denied");
        title.getStyle()
                .set("margin", "0")
                .set("color", "var(--lumo-error-text-color)");

        // Main message
        Paragraph message = new Paragraph(
                "You don't have permission to access this resource."
        );
        message.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-top", "var(--lumo-space-s)")
                .set("margin-bottom", "var(--lumo-space-xs)")
                .set("text-align", "center");

        // Details message (can be customized via query parameter)
        messageDetails = new Paragraph(
                "If you believe you should have access, please contact your administrator."
        );
        messageDetails.getStyle()
                .set("font-size", "var(--lumo-font-size-m)")
                .set("color", "var(--lumo-tertiary-text-color)")
                .set("margin-top", "0")
                .set("margin-bottom", "var(--lumo-space-l)")
                .set("text-align", "center")
                .set("max-width", "500px");

        // Action buttons container
        VerticalLayout buttonsLayout = new VerticalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(false);
        buttonsLayout.setAlignItems(Alignment.CENTER);

        // Go back button
        Button backButton = new Button("Go Back", VaadinIcon.ARROW_LEFT.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().getHistory().back()));

        // Home button
        Button homeButton = new Button("Go to Home", VaadinIcon.HOME.create());
        homeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        homeButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        buttonsLayout.add(backButton, homeButton);

        // Content container for better styling
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setAlignItems(Alignment.CENTER);
        contentLayout.setSpacing(false);
        contentLayout.setPadding(true);
        contentLayout.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("padding", "var(--lumo-space-xl)")
                .set("max-width", "600px");

        contentLayout.add(errorIcon, title, message, messageDetails, buttonsLayout);

        add(contentLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if there's a custom message in query parameters
        java.util.Map<String, java.util.List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
        if (parameters.containsKey("message")) {
            parameters.get("message")
                    .stream()
                    .findFirst()
                    .ifPresent(messageDetails::setText);
        }

        // Check if there's a reason parameter
        if (parameters.containsKey("reason")) {
            parameters.get("reason")
                    .stream()
                    .findFirst()
                    .ifPresent(reason -> {
                        switch (reason.toLowerCase()) {
                            case "insufficient_roles":
                                messageDetails.setText(
                                        "Your current role doesn't have permission to view this page. " +
                                        "Please contact your administrator if you need access."
                                );
                                break;
                            case "readonly":
                                messageDetails.setText(
                                        "You have read-only access to this resource. " +
                                        "Write permissions are required to perform this action."
                                );
                                break;
                            default:
                                break;
                        }
                    });
        }
    }
}