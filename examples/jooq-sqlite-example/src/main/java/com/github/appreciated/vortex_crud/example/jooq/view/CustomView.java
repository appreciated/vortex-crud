package com.github.appreciated.vortex_crud.example.jooq.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomView extends VerticalLayout {
    public CustomView() {
        add(new H1("Hello from Custom View!"));
    }
}
