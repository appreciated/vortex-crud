package com.github.appreciated.flow_cms.ui.view_container;

import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Service;

@Service
public class DefaultViewContainerContainerFactory implements FlowCmsViewContainerFactory {

    public DefaultViewContainerContainerFactory() {
    }

    public Component createViewContainer(ConfigObject config) {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Text(config.get("renderer").render()));
        return layout;
    }
}
