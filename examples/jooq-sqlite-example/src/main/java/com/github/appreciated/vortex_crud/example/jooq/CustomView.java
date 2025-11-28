package com.github.appreciated.vortex_crud.example.jooq;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomView extends VerticalLayout {
    public CustomView() {
        add(new H1("Custom View"));
        add(new Div(new Text("This is a custom view integrated into Vortex CRUD.")));
    }
}
