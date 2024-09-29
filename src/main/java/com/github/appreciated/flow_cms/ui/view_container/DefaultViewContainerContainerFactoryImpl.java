package com.github.appreciated.flow_cms.ui.view_container;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.view_container.master_detail.MasterDetailComponent;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultViewContainerContainerFactoryImpl implements FlowCmsViewContainerFactory {

    private final DynamicEntityManagerService dynamicEntityManagerService;

    public DefaultViewContainerContainerFactoryImpl(DynamicEntityManagerService dynamicEntityManagerService) {
        this.dynamicEntityManagerService = dynamicEntityManagerService;
    }

    public Component createViewContainer(ConfigObject config) {
        switch (config.get("renderer").render()){
            case "master_detail" -> {
                return new MasterDetailComponent(0, config, dynamicEntityManagerService);
            }
            case "grid" -> {
                return new GridComponent(0, config, dynamicEntityManagerService);
            }
            default -> throw new IllegalStateException();
        }
    }
}
