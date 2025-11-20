package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * CustomRoute allows integration of standard Vaadin @Route views into the VortexCrud navigation menu.
 * <p>
 * Unlike other route types, CustomRoute does NOT manage data or rendering - it simply adds a menu entry
 * that links to a user-defined Vaadin view annotated with @Route.
 * </p>
 *
 * <h2>Usage Requirements</h2>
 * <p>
 * To use CustomRoute, you must:
 * <ol>
 *   <li>Create a Vaadin component annotated with @Route that specifies {@code layout = ProxyRouterLayout.class}</li>
 *   <li>The route path in @Route must match the path defined in the VortexCrud configuration</li>
 *   <li>Add a CustomRoute entry to your VortexCrud configuration pointing to that path</li>
 * </ol>
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * // 1. Create your custom Vaadin view
 * @Route(value = "dashboard", layout = ProxyRouterLayout.class)
 * public class CustomDashboardView extends VerticalLayout {
 *     public CustomDashboardView() {
 *         add(new H1("Custom Dashboard"));
 *         add(new Paragraph("This is a custom view integrated into VortexCrud menu"));
 *     }
 * }
 *
 * // 2. Add CustomRoute to your VortexCrud configuration
 * Map<String, RouteRenderer<...>> routes = Map.of(
 *     "dashboard", CustomRoute.<ModelClass, FieldType, RepositoryType>builder()
 *         .title("menu.dashboard")
 *         .iconFactory(() -> VaadinIcon.DASHBOARD.create())
 *         .build(),
 *     "projects", gridRoute  // other routes...
 * );
 * }</pre>
 *
 * <h2>Important Notes</h2>
 * <ul>
 *   <li><strong>DO NOT</strong> set a dataStoreKey - CustomRoute doesn't manage data</li>
 *   <li><strong>MUST</strong> specify {@code layout = ProxyRouterLayout.class} in your @Route annotation to get VortexCrud menu</li>
 *   <li><strong>MUST</strong> ensure the route path in configuration matches the @Route value exactly</li>
 *   <li>The custom view is responsible for all rendering and logic</li>
 *   <li>Access control (readOnlyRoles/writeRoles) works the same as other routes</li>
 * </ul>
 *
 * <h2>Common Pitfalls</h2>
 * <ul>
 *   <li>Forgetting {@code layout = ProxyRouterLayout.class} → Menu won't appear</li>
 *   <li>Path mismatch between @Route and configuration → Route won't be found</li>
 *   <li>Setting a dataStoreKey → Not needed and may cause errors</li>
 * </ul>
 *
 * @param <ModelClass> Generic type for model class (not used by CustomRoute)
 * @param <FieldType> Generic type for field type (not used by CustomRoute)
 * @param <RepositoryType> Generic type for repository type (not used by CustomRoute)
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    /**
     * Not used by CustomRoute. Always returns null.
     * CustomRoute entries don't manage data - they link to user-defined @Route views.
     */
    @Builder.Default
    private RepositoryType dataStoreKey = null;

    /**
     * The i18n key for the menu title.
     * This will be displayed in the VortexCrud navigation menu.
     */
    private String title;

    /**
     * Whether this should be the default route for the application.
     * Only one route across all routes should have this set to true.
     */
    private boolean isDefaultRoute;

    /**
     * The factory class for rendering this route.
     * Always defaults to CustomRouteFactory, which is a no-op factory.
     */
    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory =
            (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) CustomRouteFactory.class;

    /**
     * Whether this route should be hidden in the menu.
     * Set to true if you want the route accessible but not visible in navigation.
     */
    private boolean isHiddenInMenu;

    /**
     * Not used by CustomRoute. Always returns null.
     * CustomRoute doesn't have configuration - the @Route view handles everything.
     */
    @Builder.Default
    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = null;

    /**
     * Factory for creating the menu icon.
     * Example: {@code () -> VaadinIcon.DASHBOARD.create()}
     */
    private SerializableSupplier<Component> iconFactory;

    /**
     * List of roles that have write access to this route.
     * Users without these roles won't be able to access the route.
     */
    private List<String> writeRoles;

    /**
     * List of roles that have read-only access to this route.
     * This can be used for view-only access control.
     */
    private List<String> readOnlyRoles;

    /**
     * Not used by CustomRoute. Always returns null.
     * CustomRoute entries are leaf nodes in the menu hierarchy.
     */
    @Builder.Default
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions = null;
}
