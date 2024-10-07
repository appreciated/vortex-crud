package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;

public interface FlowCmsEntityDetailRendererFactory {
    EntityDetailRenderer getRenderer(DetailRenderer routeConfig);
}
