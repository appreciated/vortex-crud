package com.github.appreciated.flow_cms.ui.factories.detail;

/**
 * Interface for factories that create EntityDetailRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the DetailRenderer.
 */

public interface FlowCmsDetailFactoryRegistry {
    FlowCmsDetailFactory getFactory(String type);
}
