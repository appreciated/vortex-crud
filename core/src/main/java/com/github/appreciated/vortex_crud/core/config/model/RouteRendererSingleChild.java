package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Route type that has exactly one child.
 */
public interface RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {
    FormRouteProvider<ModelClass, FieldType, RepositoryType> form();
}
