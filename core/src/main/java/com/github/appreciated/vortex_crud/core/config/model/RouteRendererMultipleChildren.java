package com.github.appreciated.vortex_crud.core.config.model;

import java.util.Map;

/**
 * Route type that has multiple named children.
 */
public interface RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {
    Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes();
}
