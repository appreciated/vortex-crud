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
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

@Service
public class JooqFormSlideFactoryConfiguration {

    public JooqFormSlideFactoryConfiguration(
            VortexCrudDialogFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dialogFactoryRegistry,
            VortexCrudRouteFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> routeFactoryRegistry,
            VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configService,
            VortexCrudDataStoreFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dataStoreFactoryRegistry,
            VortexCrudDataStoreFieldNameResolver<TableField<?, ?>> fieldNameResolver,
            VortexCrudForeignKeyResolutionStrategy<TableField<?, ?>> foreignKeyResolutionStrategy,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            FormCreator<TableRecord<?>, TableField<?, ?>, TableImpl<?>> formCreator,
            ReflectionService<TableField<?, ?>> reflectionService
    ) {
        FormSlideFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> slideFactory =
                new FormSlideFactory<>(configService, dataStoreFactoryRegistry, fieldNameResolver,
                        foreignKeyResolutionStrategy, dataStoreUtil);
        dialogFactoryRegistry.addFactory(FormSlideFactory.class, slideFactory);
        dialogFactoryRegistry.addFactory(FormSlideRouteFactory.class, slideFactory);
        routeFactoryRegistry.addFactory((Class) FormSlideRouteFactory.class,
                new FormSlideRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator,
                        routeFactoryRegistry, fieldNameResolver, reflectionService));
    }
}
