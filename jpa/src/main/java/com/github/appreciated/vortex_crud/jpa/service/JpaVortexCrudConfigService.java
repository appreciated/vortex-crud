package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.*;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.CalendarFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.VortexCrudListColumnCallbackRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaVortexCrudConfigService implements VortexCrudConfigService<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration;
    private final Map<Class<?>, VortexCrudRouteFactory> routeFactories = new HashMap<>();
    private final Map<Class<?>, VortexCrudDialogFactory> dialogFactories = new HashMap<>();
    private final ListCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listCollectionFactory;

    public JpaVortexCrudConfigService(
            VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configurationProvider,
            List<JpaRepository<?, ?>> repositoryList,
            JpaFieldService jpaFieldService,
            JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService,
            FormCreator<JpaRepository<?, ?>, String, JpaRepository<?, ?>> formCreator,
            VortexCrudFileProviderRegistry fileProviderRegistry,
            VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver,
            ReflectionService<String> reflectionService,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            VortexCrudListColumnCallbackRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> listColumnCallbackRegistry,
            VortexCrudRbacPermissionChecker<JpaRepository<?, ?>, String, JpaRepository<?, ?>> permissionChecker,
            ManyToManyPersistenceStrategy<JpaRepository<?, ?>, String, JpaRepository<?, ?>> manyToManyPersistenceStrategy,
            VortexCrudForeignKeyResolutionStrategy<String> foreignKeyResolutionStrategy
    ) {
        // 1. Create DataStores
        Map<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> repoDataStoreMap = new HashMap<>();
        Map<Class<?>, JpaRepository<?, ?>> modelRepositoryMap = new HashMap<>();

        for (JpaRepository<?, ?> repository : repositoryList) {
            JpaRepositoryDataStore<?> dataStore = new JpaRepositoryDataStore<>(repository, jpaFieldAnnotationRegistryService, new DataStoreHooks<>());
            repoDataStoreMap.put(repository, dataStore);
            modelRepositoryMap.put(dataStore.getModelClass(), repository);
        }

        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = new HashMap<>();
        for (Map.Entry<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> entry : repoDataStoreMap.entrySet()) {
            JpaRepository<?, ?> repo = entry.getKey();
            JpaRepositoryDataStore<?> store = entry.getValue();
            Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> fields = jpaFieldService.getFieldsForDataStore(store, modelRepositoryMap);

            // Populate factoryInstance for Fields
            for (Field<?, ?, ?> field : fields.values()) {
                setField(field, "factoryInstance", instantiate(field.factory()));
            }

            DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> config = DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .factory(repo)
                    .dataStoreInstance((VortexCrudDataStore) store)
                    .fields(fields)
                    .build();
            dataStores.put(repo, config);
        }

        // 2. Create Route Factories
        routeFactories.put(GridRouteFactory.class, new GridRouteFactory<>(this, formCreator, fileProviderRegistry, fieldNameResolver, reflectionService, dataStoreUtil));
        routeFactories.put(ListRouteFactory.class, new ListRouteFactory<>(this, listColumnCallbackRegistry, formCreator, fieldNameResolver, dataStoreUtil));
        routeFactories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory<>(this, fileProviderRegistry, fieldNameResolver, reflectionService, dataStoreUtil));
        routeFactories.put(FormRouteFactory.class, new FormRouteFactory<>(this, formCreator, reflectionService, permissionChecker));
        routeFactories.put(FormSlideRouteFactory.class, new FormSlideRouteFactory<>(this, formCreator, fieldNameResolver, reflectionService, permissionChecker));
        routeFactories.put(MultiFormRouteFactory.class, new MultiFormRouteFactory<>(this, formCreator, reflectionService, permissionChecker));
        routeFactories.put(KanbanFactory.class, new KanbanFactory<>(this, formCreator, fileProviderRegistry, fieldNameResolver, reflectionService, dataStoreUtil));
        routeFactories.put(CalendarFactory.class, new CalendarFactory<>(this, formCreator, fileProviderRegistry, fieldNameResolver, reflectionService, dataStoreUtil));
        routeFactories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory<>(this, dataStoreUtil));
        routeFactories.put(CustomRouteFactory.class, new CustomRouteFactory<>());

        // 3. Create Dialog Factories
        dialogFactories.put(FormRouteFactory.class, new FormDialogFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(MultiFormRouteFactory.class, new FormDialogFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(FormSlideRouteFactory.class, new FormSlideFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>(this, manyToManyPersistenceStrategy, reflectionService, dataStoreUtil));

        // Collection Factory
        this.listCollectionFactory = new ListCollectionFactory<>(this, reflectionService, dataStoreUtil, manyToManyPersistenceStrategy);

        // 4. Build Application
        Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> providedConfig = configurationProvider.get();
        if (providedConfig.dataStores() != null) {
            dataStores.putAll(providedConfig.dataStores());
        }
        this.configuration = providedConfig.toBuilder()
                .dataStores(dataStores)
                .build();

        // 5. Populate Routes
        if (this.configuration.routes() != null) {
            traverseRoutes(this.configuration.routes().values());
        }
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration() {
        return configuration;
    }

    @Override
    public String applicationName() {
        return configuration.applicationName();
    }

    private void traverseRoutes(java.util.Collection<RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes) {
        if (routes == null) return;
        for (RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> route : routes) {
            if (route == null) continue;

            // Set Factory Instance
            if (route.factory() != null) {
                setField(route, "factoryInstance", routeFactories.get(route.factory()));
            }

            // Set Dialog Factory Instance
            // Use child's factory type to determine dialog factory
            if (route instanceof RouteRendererSingleChild) {
                RouteRenderer child = ((RouteRendererSingleChild) route).child();
                if (child != null && child.factory() != null) {
                    setField(child, "dialogFactoryInstance", dialogFactories.get(child.factory()));
                }
            }

            // Also check if the route itself needs a dialog factory (e.g. if it's a form route used as a child)
            if (route.factory() != null && dialogFactories.containsKey(route.factory())) {
                 setField(route, "dialogFactoryInstance", dialogFactories.get(route.factory()));
            }

            // Handle Configuration (ItemFactory)
            RouteRendererConfiguration config = route.configuration();
            if (config != null) {
                try {
                    // Check if it has factory field
                    java.lang.reflect.Method factoryMethod = config.getClass().getMethod("factory");
                    Class<?> factoryClass = (Class<?>) factoryMethod.invoke(config);
                    if (factoryClass != null) {
                        // Instantiate Item Factory (CardFactory etc.)
                        // Assuming no-arg constructor for ItemFactories for now
                        setField(config, "factoryInstance", instantiate(factoryClass));
                    }
                } catch (Exception e) {
                    // Ignore if no factory method
                }

                // Handle children (InternalFormElement)
                if (config.children() != null) {
                    for (InternalFormElement element : (List<InternalFormElement>) config.children()) {
                        if (element.factory() != null) {
                             // For collections, we use listCollectionFactory
                             // Check if factory class matches ListCollectionFactory or similar
                             // Or just look up in a collection factories map if we had one
                             // For now we only have ListCollectionFactory
                             setField(element, "factoryInstance", listCollectionFactory);
                        }

                        // Collection config
                        if (element.configuration() != null) {
                             Collection col = element.configuration();
                             if (col.factory() != null) {
                                 // Dialog factory for collection
                                 setField(col, "factoryInstance", dialogFactories.get(col.factory()));
                             }
                             if (col.child() != null) {
                                 traverseRoutes(List.of(col.child()));
                             }
                        }
                    }
                }

                // Handle MultiForm children
                if (config instanceof MultiFormRendererConfiguration) {
                    MultiFormRendererConfiguration multiConfig = (MultiFormRendererConfiguration) config;
                    if (multiConfig.forms() != null) {
                        for (Object childFormObj : multiConfig.forms()) {
                             RouteRendererConfiguration childForm = (RouteRendererConfiguration) childFormObj;
                             // Recurse/populate fields in child forms?
                             if (childForm.children() != null) {
                                 for (InternalFormElement element : (List<InternalFormElement>) childForm.children()) {
                                     if (element.factory() != null) {
                                         setField(element, "factoryInstance", listCollectionFactory);
                                     }
                                     if (element.configuration() != null) {
                                         Collection col = element.configuration();
                                         if (col.factory() != null) {
                                             setField(col, "factoryInstance", dialogFactories.get(col.factory()));
                                         }
                                         if (col.child() != null) {
                                             traverseRoutes(List.of(col.child()));
                                         }
                                     }
                                 }
                             }
                        }
                    }
                }
            }

            // Recurse
            if (route instanceof RouteRendererMultipleChildren) {
                Map<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> children = ((RouteRendererMultipleChildren) route).childrenMap();
                if (children != null) {
                    traverseRoutes(children.values());
                }
            } else if (route instanceof RouteRendererSingleChild) {
                RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> child = ((RouteRendererSingleChild) route).child();
                if (child != null) {
                    traverseRoutes(List.of(child));
                }
            }
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        if (target == null) return;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate " + clazz.getName(), e);
        }
    }
}
