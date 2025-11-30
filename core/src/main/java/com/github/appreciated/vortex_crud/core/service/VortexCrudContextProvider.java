package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.VortexCrudListColumnCallbackRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VortexCrudContextProvider<ModelClass, FieldType, RepositoryType> {

    @Autowired
    private VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    @Autowired
    private VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    @Autowired
    private VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy;
    @Autowired
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;
    @Autowired
    private ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy;
    @Autowired
    private ReflectionService<FieldType> reflectionService;
    @Autowired(required = false)
    private VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;
    @Autowired
    private VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry;
    @Autowired
    private VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry;
    @Autowired
    private VortexCrudCollectionFactoryRegistry<ModelClass, FieldType, RepositoryType> collectionFactoryRegistry;
    @Autowired
    private VortexCrudFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> fieldFactoryRegistry;
    @Autowired
    private VortexCrudItemFactoryRegistry<FieldType> itemFactoryRegistry;
    @Autowired
    private VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> listColumnCallbackRegistry;
    @Autowired
    private VortexCrudFileProviderRegistry fileProviderRegistry;
    @Autowired
    private FormCreator<ModelClass, FieldType, RepositoryType> formCreator;

    public VortexCrudContext<ModelClass, FieldType, RepositoryType> getContext() {
        return VortexCrudContext.<ModelClass, FieldType, RepositoryType>builder()
                .configService(configService)
                .fieldNameResolver(fieldNameResolver)
                .foreignKeyResolutionStrategy(foreignKeyResolutionStrategy)
                .dataStoreUtil(dataStoreUtil)
                .manyToManyPersistenceStrategy(manyToManyPersistenceStrategy)
                .reflectionService(reflectionService)
                .permissionChecker(permissionChecker)
                .routeFactoryRegistry(routeFactoryRegistry)
                .dialogFactoryRegistry(dialogFactoryRegistry)
                .collectionFactoryRegistry(collectionFactoryRegistry)
                .fieldFactoryRegistry(fieldFactoryRegistry)
                .itemFactoryRegistry(itemFactoryRegistry)
                .listColumnCallbackRegistry(listColumnCallbackRegistry)
                .fileProviderRegistry(fileProviderRegistry)
                .formCreator(formCreator)
                .build();
    }
}
