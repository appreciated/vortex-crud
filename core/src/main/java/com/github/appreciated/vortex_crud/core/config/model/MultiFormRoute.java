package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Configuration model for rendering multiple forms simultaneously on a single route.
 * This route type allows displaying multiple form configurations side by side or stacked,
 * useful for complex data entry scenarios or related entity management.
 *
 * <p>Unlike {@link FormRoute} which renders a single form, MultiFormRoute can display
 * multiple form configurations that may represent different aspects of the same entity
 * or related entities that need to be managed together.</p>
 *
 * @param <ModelClass> the entity type this route operates on
 * @param <FieldType> the field type (JPA Field or jOOQ TableField)
 * @param <RepositoryType> the repository or table type
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultiFormRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    /**
     * The data store key (repository or table) that this route operates on.
     */
    private RepositoryType dataStoreKey;

    /**
     * The title displayed for this route in the navigation menu.
     */
    private String title;

    /**
     * Whether this route should be the default route when the application starts.
     */
    private boolean isDefaultRoute;

    /**
     * The factory class responsible for rendering this route.
     * Defaults to {@link MultiFormRouteFactory}.
     */
    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) MultiFormRouteFactory.class;

    /**
     * Whether this route should be hidden from the navigation menu.
     */
    private boolean isHiddenInMenu;

    /**
     * Configuration for rendering multiple forms, including the list of form configurations
     * to be displayed together.
     */
    private MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    /**
     * Factory for creating the icon displayed in the navigation menu.
     */
    private SerializableSupplier<Component> iconFactory;

    /**
     * List of roles that have write access to this route.
     */
    private List<String> writeRoles;

    /**
     * List of roles that have read-only access to this route.
     */
    private List<String> readOnlyRoles;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    @Override
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
