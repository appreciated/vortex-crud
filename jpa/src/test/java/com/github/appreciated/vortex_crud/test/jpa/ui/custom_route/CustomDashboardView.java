package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.core.ui.routes.ProxyRouterLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Example custom Vaadin view that integrates with VortexCrud menu system.
 * <p>
 * This demonstrates how to create a custom view that:
 * <ul>
 *   <li>Uses standard Vaadin @Route annotation</li>
 *   <li>Specifies layout = ProxyRouterLayout.class to get VortexCrud menu</li>
 *   <li>Has full control over rendering and logic</li>
 *   <li>Appears in VortexCrud navigation via CustomRoute configuration</li>
 * </ul>
 * </p>
 */
@Route(value = "dashboard", layout = ProxyRouterLayout.class)
public class CustomDashboardView extends VerticalLayout {

    public CustomDashboardView() {
        // Custom view styling
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Custom content
        H1 title = new H1("Custom Dashboard");
        Paragraph description = new Paragraph("This is a custom dashboard integrated into VortexCrud");
        description.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // Example custom component
        Div customContent = new Div();
        customContent.setText("You have full control over this view's content and logic");
        customContent.getStyle()
                .set("padding", "2em")
                .set("background-color", "var(--lumo-contrast-10pct)")
                .set("border-radius", "var(--lumo-border-radius)");

        // Add all components
        add(title, description, customContent);
    }
}
