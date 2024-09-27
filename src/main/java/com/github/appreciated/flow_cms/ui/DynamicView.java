package com.github.appreciated.flow_cms.ui;

import com.github.appreciated.flow_cms.entity.ViewConfig;
import com.github.appreciated.flow_cms.service.ViewConfigService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("dynamic-view")
public class DynamicView extends VerticalLayout {

    @Autowired
    private ViewConfigService viewConfigService;

    @PostConstruct
    public void init() {
        List<ViewConfig> viewConfigs = viewConfigService.findAll();
        for (ViewConfig viewConfig : viewConfigs) {
            add(createComponentFromConfig(viewConfig));
        }
    }

    private Component createComponentFromConfig(ViewConfig viewConfig) {
        VerticalLayout layout = new VerticalLayout();
        viewConfig.getLayout().forEach(config -> {
            Component component = ComponentListFactory.createComponent(config.getComponent());
            layout.add(component);
        });
        return layout;
    }
}
