package com.github.appreciated.flow_cms.ui.view_container;

import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import org.springframework.stereotype.Service;

@Service
public class DefaultViewContainerContainerFactoryImpl implements FlowCmsViewContainerFactory {

    public DefaultViewContainerContainerFactoryImpl() {
    }

    public Component createViewContainer(ConfigObject config) {
        switch (config.get("renderer").render()){
            case "master_detail" -> {
                return new MasterDetailComponent(0, null,null);
            }
            default -> throw new IllegalStateException();
        }
    }
}
