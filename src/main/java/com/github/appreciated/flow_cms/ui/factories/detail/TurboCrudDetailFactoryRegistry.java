package com.github.appreciated.flow_cms.ui.factories.detail;

/**
 * Interface for factories that create {@link TurboCrudDetailFactory} instances.
 * This factory provides the appropriate renderer for types specified in the DetailRenderer.
 */

public interface TurboCrudDetailFactoryRegistry {
    TurboCrudDetailFactory getFactory(String type);
}
