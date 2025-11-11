package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * A route configuration for integrating custom Vaadin views (annotated with @Route) into the VortexCrud menu system.
 * This allows users to create their own views with full control and add them to the VortexCrud navigation menu.
 *
 * <p><strong>Important:</strong> CustomRoute is ONLY a menu entry. The actual routing is handled entirely by
 * Vaadin's @Route annotation. You must ensure the @Route path matches the configuration key.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // 1. Create your custom view with @Route annotation:
 * @Route(value = "dashboard", layout = ProxyRouterLayout.class)  // MUST specify layout for menu
 * public class DashboardView extends VerticalLayout {
 *     public DashboardView() {
 *         add(new H1("My Custom Dashboard"));
 *         // ... your custom logic ...
 *     }
 * }
 *
 * // 2. Add it to VortexCrud menu in your configuration:
 * routes.put("dashboard", CustomRoute.builder()  // Key must match @Route path
 *     .title("menu.dashboard")
 *     .iconFactory(() -> VaadinIcon.DASHBOARD.create())
 *     .build());
 * }</pre>
 *
 * <p><strong>Common Pitfalls:</strong></p>
 * <ul>
 *   <li><strong>Missing layout:</strong> You MUST specify {@code layout = ProxyRouterLayout.class} in your
 *       @Route annotation, otherwise your view won't have the VortexCrud menu/navigation.</li>
 *   <li><strong>Path mismatch:</strong> If your @Route("foo") doesn't match routes.put("bar", ...),
 *       the menu will navigate to the wrong path. Always ensure they match.</li>
 *   <li><strong>No dataStoreKey:</strong> CustomRoute doesn't require a dataStoreKey since custom views
 *       manage their own data. Don't use menuActions that require a dataStore.</li>
 * </ul>
 *
 * @param <ModelClass>     The entity model class
 * @param <FieldType>      The field type (typically an enum or descriptor)
 * @param <RepositoryType> The repository/data store key type
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    /**
     * Optional data store key. Not required for custom routes since they manage their own data.
     */
    @Nullable
    private RepositoryType dataStoreKey;

    /**
     * The translation key for the menu item title (e.g., "menu.dashboard").
     */
    private String title;

    private boolean isDefaultRoute;

    /**
     * Factory is not used for CustomRoute since routing is handled by Vaadin's @Route system.
     * A no-op factory is provided for interface compatibility.
     */
    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) CustomRouteFactory.class;

    private boolean isHiddenInMenu;

    @Nullable
    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     * <p><strong>Warning:</strong> If using menuActions that require a dataStore, you must provide
     * a valid dataStoreKey. Otherwise, a runtime exception will occur when the menu action is rendered.</p>
     */
    @Nullable
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    @Override
    @Nullable
    public RepositoryType dataStoreKey() {
        return dataStoreKey;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public List<String> writeRoles() {
        return writeRoles;
    }

    @Override
    public List<String> readOnlyRoles() {
        return readOnlyRoles;
    }
}
