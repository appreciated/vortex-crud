package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
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
 * Adds a custom component to the VortexCrud menu.
 * Just pass your component class and it will be registered as a route with ProxyRouterLayout.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CustomRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    private Class<? extends Component> componentClass;

    @Builder.Default
    private DataStoreProvider<ModelClass, FieldType, RepositoryType> dataStore = null;

    public static class CustomRouteBuilder<ModelClass, FieldType, RepositoryType> {
        private DataStoreProvider<ModelClass, FieldType, RepositoryType> dataStore;
        public CustomRouteBuilder<ModelClass, FieldType, RepositoryType> dataStoreKey(RepositoryType key) {
            this.dataStore = new KeyDataStoreProvider<>(key);
            return this;
        }

        public CustomRouteBuilder<ModelClass, FieldType, RepositoryType> dataStore(VortexCrudDataStore<FieldType, ModelClass> store) {
            this.dataStore = new InstanceDataStoreProvider<>(store);
            return this;
        }

        public CustomRouteBuilder<ModelClass, FieldType, RepositoryType> dataStore(VortexCrudDataStore<FieldType, ModelClass> store, DataStoreConfig<ModelClass, FieldType, RepositoryType> config) {
            this.dataStore = new InstanceDataStoreProvider<>(store, config);
            return this;
        }
    }

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory =
            (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) CustomRouteFactory.class;

    private boolean isHiddenInMenu;

    @Builder.Default
    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration = null;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    @Builder.Default
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions = null;
}
