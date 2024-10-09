package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;

/**
 * Interface for factories that create EntityDetailRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the DetailRenderer.
 */

public interface FlowCmsEntityDetailRendererFactory {
    FlowCmsEntityDetailRenderer getRenderer(DetailRenderer routeConfig);
}
