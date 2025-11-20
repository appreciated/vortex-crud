package com.github.appreciated.vortex_crud.core.ui.factories.route.custom;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import jakarta.annotation.Nullable;

/**
 * Factory for CustomRoute that provides a no-op implementation.
 * <p>
 * CustomRoute entries link to user-defined Vaadin views with @Route annotations.
 * These views are registered and rendered by Vaadin's routing mechanism, not by VortexCrud.
 * Therefore, this factory should never actually be called during normal operation.
 * </p>
 *
 * <p>
 * If this factory's renderRoute() method is invoked, it indicates a configuration error:
 * <ul>
 *   <li>The @Route path doesn't match the CustomRoute configuration path</li>
 *   <li>The @Route annotation is missing {@code layout = ProxyRouterLayout.class}</li>
 *   <li>The custom view class isn't properly annotated with @Route</li>
 * </ul>
 * </p>
 *
 * <h2>How CustomRoute Works</h2>
 * <ol>
 *   <li>User creates a view with {@code @Route(value = "path", layout = ProxyRouterLayout.class)}</li>
 *   <li>User adds CustomRoute to VortexCrud configuration with matching path</li>
 *   <li>DynamicRouteGenerator skips registering CustomRoute (already registered via @Route)</li>
 *   <li>CustomRoute entry appears in menu, links to the @Route view</li>
 *   <li>Vaadin's routing handles navigation to the custom view</li>
 *   <li>This factory is never called</li>
 * </ol>
 *
 * @param <ModelClass> Generic type for model class (not used)
 * @param <FieldType> Generic type for field type (not used)
 * @param <RepositoryType> Generic type for repository type (not used)
 */
public class CustomRouteFactory<ModelClass, FieldType, RepositoryType>
        implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    /**
     * This method should never be called during normal operation.
     * If it is called, it indicates a configuration error with the custom route.
     *
     * @param currentPathIndex The current index in the navigation path
     * @param routeResolver The route resolver
     * @param detailRouteSetting Detail route settings (if any)
     * @return An error message component
     */
    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        // This should never be called - CustomRoute views are handled by @Route annotation
        Div errorContainer = new Div();
        errorContainer.getStyle().set("padding", "2em");

        Paragraph errorMessage = new Paragraph(
                "ERROR: CustomRouteFactory.renderRoute() was called. This should never happen! " +
                "Verify your custom route configuration: " +
                "1) Check that your @Route annotation includes 'layout = ProxyRouterLayout.class'. " +
                "2) Ensure the @Route path matches the CustomRoute configuration path. " +
                "3) Verify the custom view class is a valid Vaadin component."
        );
        errorMessage.getStyle()
                .set("color", "red")
                .set("font-weight", "bold")
                .set("padding", "1em")
                .set("border", "2px solid red")
                .set("border-radius", "4px")
                .set("background-color", "#ffebee");

        errorContainer.add(errorMessage);
        return errorContainer;
    }

    /**
     * CustomRoute is not a container route - it's a leaf node that links to a user view.
     *
     * @return false
     */
    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
