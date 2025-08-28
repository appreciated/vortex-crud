package com.github.appreciated.vortex_crud.test.jpa.ui.form_slide;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormSlideFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class JpaFormSlideFactoryConfiguration {

    public JpaFormSlideFactoryConfiguration(
            VortexCrudDialogFactoryRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dialogFactoryRegistry,
            VortexCrudRouteFactoryRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> routeFactoryRegistry,
            VortexCrudConfigService<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configService,
            VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> dataStoreFactoryRegistry,
            VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver,
            VortexCrudForeignKeyResolutionStrategy<String> foreignKeyResolutionStrategy,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            FormCreator<JpaRepository<?, ?>, String, JpaRepository<?, ?>> formCreator,
            ReflectionService<String> reflectionService
    ) {
        FormSlideFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> slideFactory =
                new FormSlideFactory<>(configService, dataStoreFactoryRegistry, fieldNameResolver,
                        foreignKeyResolutionStrategy, dataStoreUtil);
        dialogFactoryRegistry.addFactory(FormSlideFactory.class, slideFactory);
        dialogFactoryRegistry.addFactory(FormSlideRouteFactory.class, slideFactory);
        routeFactoryRegistry.addFactory((Class) FormSlideRouteFactory.class,
                new FormSlideRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator,
                        routeFactoryRegistry, fieldNameResolver, reflectionService));
    }
}
