package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Interface for routes that provide form rendering functionality.
 * Implemented by routes that need to render forms, such as FormRoute,
 * MasterDetailRoute, and FormSlideRoute.
 */
public interface FormRouteProvider<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    /**
     * Gets the form renderer configuration for this route.
     *
     * @return the form renderer configuration
     */
    FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration();

    boolean isDeleteButtonHidden();
}
