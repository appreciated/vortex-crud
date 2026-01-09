package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SubmenuRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> {

    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @I18nKey
    @lombok.NonNull
    private String title;

    private boolean defaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new SubmenuRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = null;

    private boolean hiddenInMenu;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    @lombok.NonNull
    private Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

    @Override
    public List<com.github.appreciated.vortex_crud.core.ui.actions.RouteAction<FieldType, ModelClass>> routeActions() {
        return null;
    }
}
