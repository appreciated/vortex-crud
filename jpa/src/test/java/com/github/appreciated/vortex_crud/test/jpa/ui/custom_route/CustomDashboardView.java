package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Example custom view that demonstrates how to create a custom Vaadin view
 * and integrate it into the VortexCrud menu system using CustomRoute.
 * <p>
 * This view:
 * - Is annotated with @Route to make it a Vaadin route
 * - Can inject Spring beans for accessing data and services
 * - Has complete control over its rendering and logic
 * - Gets added to the menu via CustomRoute configuration
 */
@Route("dashboard")
public class CustomDashboardView extends VerticalLayout {

    @Autowired
    public CustomDashboardView(JpaCustomRouteTestRepository repository) {
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Custom Dashboard");
        title.getStyle().set("color", "var(--lumo-primary-text-color)");

        Paragraph description = new Paragraph(
                "This is a custom view created with @Route annotation and integrated into VortexCrud menu."
        );

        // Access repository to show data
        long itemCount = repository.count();
        Div statsCard = new Div();
        statsCard.getStyle()
                .set("padding", "var(--lumo-space-m)")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border-radius", "var(--lumo-border-radius-m)");
        statsCard.add(new Paragraph("Total Items: " + itemCount));

        Paragraph info = new Paragraph(
                "You have complete control over this view - add charts, forms, custom layouts, etc."
        );

        add(title, description, statsCard, info);
    }
}
