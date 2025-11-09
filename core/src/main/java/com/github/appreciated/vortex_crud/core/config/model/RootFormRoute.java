package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Configuration for a form route that edits a specific single entry based on filtering,
 * rather than using an ID from the URL path.
 * <p>
 * This is useful for routes like user profiles where the entity is determined by
 * the current user context (e.g., Spring Security) rather than a URL parameter.
 * <p>
 * Example:
 * <pre>
 * RootFormRoute.builder()
 *     .dataStoreKey(userRepository)
 *     .title("route.profile.title")
 *     .entityFilterField("username")
 *     .entityFilterValueProvider(() -> getCurrentUsername())
 *     .configuration(...)
 *     .build()
 * </pre>
 *
 * @param <ModelClass>    The entity class type
 * @param <FieldType>     The field type (e.g., String for JPA, TableField for jOOQ)
 * @param <RepositoryType> The repository/table type
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RootFormRoute<ModelClass, FieldType, RepositoryType>  implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) FormRouteFactory.class;

    private boolean isHiddenInMenu;

    private FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private List<? extends InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    public RepositoryType getDataStoreKey() {
        return dataStoreKey;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getWriteRoles() {
        return writeRoles;
    }

    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }


    private FieldType entityFilterField;

    /**
     * Provider that supplies the filter value dynamically (e.g., from security context).
     */
    private SerializableSupplier<Object> entityFilterValueProvider;

    @Override
    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration() {
        return formConfiguration;
    }

    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;
}
