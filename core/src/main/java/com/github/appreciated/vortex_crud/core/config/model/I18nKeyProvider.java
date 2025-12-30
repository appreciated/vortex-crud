package com.github.appreciated.vortex_crud.core.config.model;

import java.util.Collection;

/**
 * Interface for configuration objects that contain internationalization keys.
 */
public interface I18nKeyProvider {
    /**
     * Returns a collection of i18n keys used by this configuration object and its children.
     *
     * @return a collection of i18n keys
     */
    Collection<String> getI18nKeys();
}
