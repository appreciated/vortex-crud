package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Route type that has exactly one child.
 */
public interface RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {
    RouteRenderer<ModelClass, FieldType, RepositoryType> form();

    @Override
    default java.util.Collection<String> getI18nKeys() {
        java.util.Collection<String> keys = RouteRenderer.super.getI18nKeys();
        if (form() != null) {
            keys.addAll(form().getI18nKeys());
        }
        return keys;
    }
}
