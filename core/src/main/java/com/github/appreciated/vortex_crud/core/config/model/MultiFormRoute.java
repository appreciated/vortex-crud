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
 * Configuration for a multi-form route that displays multiple forms simultaneously
 * for a single entity. This is useful for complex forms that need to be organized
 * into multiple sections or tabs.
 * <p>
 * Example:
 * <pre>
 * MultiFormRoute.builder()
 *     .dataStoreKey(userRepository)
 *     .title("route.user.profile.title")
 *     .configuration(MultiFormRendererConfiguration.builder()
 *         .forms(List.of(
 *             FormRendererConfiguration.builder()...build(),
 *             FormRendererConfiguration.builder()...build()
 *         ))
 *         .build())
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
@Builder
@Getter
public class MultiFormRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) MultiFormRouteFactory.class;

    private boolean isHiddenInMenu;

    private MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;
}
