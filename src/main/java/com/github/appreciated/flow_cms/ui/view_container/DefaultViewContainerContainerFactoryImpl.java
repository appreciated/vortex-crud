package com.github.appreciated.flow_cms.ui.view_container;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.view_container.grid.GridContainer;
import com.github.appreciated.flow_cms.ui.view_container.master_detail.MasterDetailContainer;
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
        String renderer = config.toConfig().getString("renderer");
        switch (renderer){
            case "master_detail" -> {
                return new MasterDetailContainer(0, config, dynamicEntityManagerService);
            }
            case "grid" -> {
                return new GridContainer(0, config, dynamicEntityManagerService);
            }
            default -> throw new IllegalStateException();
        }
    }
}
