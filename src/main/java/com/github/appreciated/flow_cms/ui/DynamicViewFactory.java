package com.github.appreciated.flow_cms.ui;

import com.github.appreciated.flow_cms.service.ViewConfigParser;
import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.List;

public class DynamicViewFactory {

    private ViewConfigParser configParser;

    public DynamicViewFactory(ViewConfigParser configParser) {
        this.configParser = configParser;
    }

    public Component createView(String viewName) {
        List<? extends Config> fields = configParser.getFieldsForView(viewName);
        VerticalLayout layout = new VerticalLayout();

        for (Config field : fields) {
            String componentType = field.getString("component");
            String fieldName = field.getString("field");
            Component component = ComponentListFactory.createComponent(componentType);
            layout.add(component);
        }

        return layout;
    }
}
