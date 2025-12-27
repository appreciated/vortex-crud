package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

/**
 * Route factory equivalent to {@link FormRouteFactory} used for slide-in dialogs.
 * Functionality is identical to {@link FormRouteFactory}; the class acts as a marker to
 * select {@code FormSlideFactory} as dialog provider.
 */
public class FormSlideRouteFactory<ModelClass, FieldType, RepositoryType>
        extends FormRouteFactory<ModelClass, FieldType, RepositoryType> {

}
