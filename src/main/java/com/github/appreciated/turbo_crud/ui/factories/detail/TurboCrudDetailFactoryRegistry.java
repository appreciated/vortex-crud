package com.github.appreciated.turbo_crud.ui.factories.detail;

/**
 * Interface for factories that create {@link TurboCrudDetailFactory} instances.
 * This factory provides the appropriate renderer for types specified in the DetailRenderer.
 */

public interface TurboCrudDetailFactoryRegistry {
    TurboCrudDetailFactory getFactory(String type);
}
