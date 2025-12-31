package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.Collections;
import java.util.List;

/**
 * Base interface for all route renderers. Child navigation specifics are defined in specialized sub-interfaces.
 */
public interface RouteRenderer<ModelClass, FieldType, RepositoryType> extends AccessControlled, HasDataStore<FieldType, ModelClass>, ItemFactory<FieldType>, ValidatableConfiguration {

    DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig();

    @Override
    default VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig() != null ? dataStoreConfig().dataStoreInstance() : null;
    }

    String title();

    boolean isDefaultRoute();

    VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory();

    default VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory() {
        return null;
    }

    boolean isHiddenInMenu();

    default VortexCrudItemFactory<FieldType> itemFactory() {
        return null;
    }

    default FieldType titleField() {
        return null;
    }

    default FieldType descriptionField() {
        return null;
    }

    default FieldType imageField() {
        return null;
    }

    default VortexCrudResourceProvider resourceProvider() {
        return null;
    }

    default boolean inlineEdit() {
        return false;
    }

    default FieldType filterField() {
        return null;
    }

    default List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children() {
        return Collections.emptyList();
    }

    SerializableSupplier<Component> iconFactory();

    List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions();

    default java.util.List<RouteFilter<FieldType>> filters() {
        return java.util.Collections.emptyList();
    }

    /**
     * List of custom route actions with full access to data store and selected entities.
     * These actions will be rendered in the route header and automatically
     * enabled/disabled based on selection state.
     *
     * @return The list of route actions, or null if none
     */
    default List<RouteAction<FieldType, ModelClass>> routeActions() {
        return null;
    }
}
