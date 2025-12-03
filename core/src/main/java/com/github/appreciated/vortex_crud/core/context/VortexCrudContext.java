package com.github.appreciated.vortex_crud.core.context;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudPermissionResolutionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.VortexCrudListColumnCallbackRegistry;
import org.springframework.stereotype.Component;

@Component
public class VortexCrudContext<ModelClass, FieldType, RepositoryType> {
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudPermissionResolutionService permissionResolutionService;
    private final TranslationService translationService;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry;
    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> rbacPermissionChecker;
    private final VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy;
    private final ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy;

    public VortexCrudContext(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            ReflectionService<FieldType> reflectionService,
            VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            VortexCrudPermissionResolutionService permissionResolutionService,
            TranslationService translationService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry,
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> rbacPermissionChecker,
            VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy,
            ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy
    ) {
        this.configService = configService;
        this.reflectionService = reflectionService;
        this.fieldNameResolver = fieldNameResolver;
        this.dataStoreUtil = dataStoreUtil;
        this.permissionResolutionService = permissionResolutionService;
        this.translationService = translationService;
        this.formCreator = formCreator;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.rbacPermissionChecker = rbacPermissionChecker;
        this.foreignKeyResolutionStrategy = foreignKeyResolutionStrategy;
        this.manyToManyPersistenceStrategy = manyToManyPersistenceStrategy;
    }

    public VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService() { return configService; }
    public ReflectionService<FieldType> reflectionService() { return reflectionService; }
    public VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver() { return fieldNameResolver; }
    public VortexCrudDataStoreUtilStrategy dataStoreUtil() { return dataStoreUtil; }
    public VortexCrudPermissionResolutionService permissionResolutionService() { return permissionResolutionService; }
    public TranslationService translationService() { return translationService; }
    public FormCreator<ModelClass, FieldType, RepositoryType> formCreator() { return formCreator; }
    public VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry() { return columnCallbackRegistry; }
    public VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> rbacPermissionChecker() { return rbacPermissionChecker; }
    public VortexCrudForeignKeyResolutionStrategy<FieldType> foreignKeyResolutionStrategy() { return foreignKeyResolutionStrategy; }
    public ManyToManyPersistenceStrategy<ModelClass, FieldType, RepositoryType> manyToManyPersistenceStrategy() { return manyToManyPersistenceStrategy; }
}
