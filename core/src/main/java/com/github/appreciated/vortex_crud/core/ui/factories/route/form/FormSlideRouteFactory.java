package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;

/**
 * Route factory equivalent to {@link FormRouteFactory} used for slide-in dialogs.
 * Functionality is identical to {@link FormRouteFactory}; the class acts as a marker to
 * select {@code FormSlideFactory} as dialog provider.
 */
public class FormSlideRouteFactory<DataStoreId, FieldId, KeyType>
        extends FormRouteFactory<DataStoreId, FieldId, KeyType> {

    public FormSlideRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                                 VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                                 FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                                 VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> factoryRegistry,
                                 VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                                 ReflectionService<FieldId> reflectionService) {
        super(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry, fieldNameResolver, reflectionService);
    }
}
