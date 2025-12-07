package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomDashboardView extends VerticalLayout {

    public CustomDashboardView() {
        setSizeFull();
        setPadding(true);
        add(new H1("Custom Dashboard"));
        add(new Paragraph("This is a custom dashboard integrated into VortexCrud"));
    }
}
