package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.List;

/**
 * Interface for routes that provide form rendering functionality.
 * Interface implemented by route types that provide form configuration, such as FormRoute
 * and MasterDetailRoute.
 */
public interface FormRouteProvider<ModelClass, FieldType, RepositoryType> extends RouteRenderer<ModelClass, FieldType, RepositoryType> {

    boolean isDeleteButtonHidden();

    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> fields();

    FormRouteProvider<ModelClass, FieldType, RepositoryType> dataStoreConfig(DataStoreConfig<ModelClass, FieldType, RepositoryType> config);

    FormRouteProvider<ModelClass, FieldType, RepositoryType> title(String title);
}
