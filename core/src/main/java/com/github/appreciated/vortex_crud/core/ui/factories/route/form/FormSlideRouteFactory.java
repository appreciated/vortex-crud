package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;

/**
 * Route factory equivalent to {@link FormRouteFactory} used for slide-in dialogs.
 * Functionality is identical to {@link FormRouteFactory}; the class acts as a marker to
 * select {@code FormSlideFactory} as dialog provider.
 */
public class FormSlideRouteFactory<ModelClass, FieldType, RepositoryType>
        extends FormRouteFactory<ModelClass, FieldType, RepositoryType> {

    public FormSlideRouteFactory(
                                 VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                 FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                                 VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> factoryRegistry,
                                 VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                                 ReflectionService<FieldType> reflectionService,
                                 VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker
    ) {
        super(configService, formCreator, factoryRegistry, reflectionService,permissionChecker);
    }
}
