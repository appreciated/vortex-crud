package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

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
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
public class JooqFormSlideFactoryConfiguration {

    public JooqFormSlideFactoryConfiguration(
            VortexCrudDialogFactoryRegistry<DSLContext, String, DSLContext> dialogFactoryRegistry,
            VortexCrudRouteFactoryRegistry<DSLContext, String, DSLContext> routeFactoryRegistry,
            VortexCrudConfigService<DSLContext, String, DSLContext> configService,
            VortexCrudDataStoreFactoryRegistry<DSLContext, String, DSLContext> dataStoreFactoryRegistry,
            VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver,
            VortexCrudForeignKeyResolutionStrategy<String> foreignKeyResolutionStrategy,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            FormCreator<DSLContext, String, DSLContext> formCreator,
            ReflectionService<String> reflectionService
    ) {
        FormSlideFactory<DSLContext, String, DSLContext> slideFactory =
                new FormSlideFactory<>(configService, dataStoreFactoryRegistry, fieldNameResolver,
                        foreignKeyResolutionStrategy, dataStoreUtil);
        dialogFactoryRegistry.addFactory(FormSlideFactory.class, slideFactory);
        dialogFactoryRegistry.addFactory(FormSlideRouteFactory.class, slideFactory);
        routeFactoryRegistry.addFactory((Class) FormSlideRouteFactory.class,
                new FormSlideRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator,
                        routeFactoryRegistry, fieldNameResolver, reflectionService));
    }
}
