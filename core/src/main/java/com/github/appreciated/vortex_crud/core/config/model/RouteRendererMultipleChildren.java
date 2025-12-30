package com.github.appreciated.vortex_crud.core.config.model;

import java.util.Map;

/**
 * Route type that has multiple named children.
 */
public interface RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {
    Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap();

    @Override
    default java.util.Collection<String> getI18nKeys() {
        java.util.Collection<String> keys = RouteRenderer.super.getI18nKeys();
        if (childrenMap() != null) {
            childrenMap().values().forEach(child -> keys.addAll(child.getI18nKeys()));
        }
        return keys;
    }
}
