package com.github.appreciated.vortex_crud.jooq.service;

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
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JooqVortexCrudConfigService implements VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration;
    private final Map<Class<?>, VortexCrudRouteFactory> routeFactories = new HashMap<>();
    private final Map<Class<?>, VortexCrudDialogFactory> dialogFactories = new HashMap<>();
    private final ListCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> listCollectionFactory;

    public JooqVortexCrudConfigService(
            VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configurationProvider,
            DSLContext dslContext,
            // Dependencies for factories
            FormCreator<TableRecord<?>, TableField<?, ?>, TableImpl<?>> formCreator,
            VortexCrudFileProviderRegistry fileProviderRegistry,
            VortexCrudDataStoreFieldNameResolver<TableField<?, ?>> fieldNameResolver,
            ReflectionService<TableField<?, ?>> reflectionService,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            VortexCrudListColumnCallbackRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> listColumnCallbackRegistry,
            VortexCrudRbacPermissionChecker<TableRecord<?>, TableField<?, ?>, TableImpl<?>> permissionChecker,
            ManyToManyPersistenceStrategy<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToManyPersistenceStrategy,
            VortexCrudForeignKeyResolutionStrategy<TableField<?, ?>> foreignKeyResolutionStrategy
    ) {
        // 1. Build initial configuration
        this.configuration = configurationProvider.get();

        // 2. Create DataStores
        for (Map.Entry<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> entry : configuration.dataStores().entrySet()) {
            TableImpl<?> table = entry.getKey();
            DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config = entry.getValue();
            Class<?> recordType = table.getRecordType();

            VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> dataStore = new JooqDataStore(recordType, dslContext, config.hooks());

            // Set dataStoreInstance
            // DataStoreConfig is likely immutable, but we need to set the field.
            // Since we have the config object, we can use reflection.
            setField(config, "dataStoreInstance", dataStore);

            // Populate fields factoryInstance
            if (config.fields() != null) {
                for (Field<?, ?, ?> field : config.fields().values()) {
                    setField(field, "factoryInstance", instantiate(field.factory()));
                }
            }
        }

        // 3. Create Route Factories
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

        // 4. Create Dialog Factories
        dialogFactories.put(FormRouteFactory.class, new FormDialogFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(MultiFormRouteFactory.class, new FormDialogFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(FormSlideRouteFactory.class, new FormSlideFactory<>(this, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil));
        dialogFactories.put(ConnectDialogFactory.class, new ConnectDialogFactory<>(this, manyToManyPersistenceStrategy, reflectionService, dataStoreUtil));

        // Collection Factory
        this.listCollectionFactory = new ListCollectionFactory<>(this, reflectionService, dataStoreUtil, manyToManyPersistenceStrategy);

        // 5. Populate Routes
        if (this.configuration.routes() != null) {
            traverseRoutes(this.configuration.routes().values());
        }
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        return configuration;
    }

    @Override
    public String applicationName() {
        return configuration.applicationName();
    }

    private void traverseRoutes(java.util.Collection<RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes) {
        if (routes == null) return;
        for (RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> route : routes) {
            if (route == null) continue;

            // Set Factory Instance
            if (route.factory() != null) {
                setField(route, "factoryInstance", routeFactories.get(route.factory()));
            }

            // Set Dialog Factory Instance
            if (route instanceof RouteRendererSingleChild) {
                RouteRenderer child = ((RouteRendererSingleChild) route).child();
                if (child != null && child.factory() != null) {
                    setField(child, "dialogFactoryInstance", dialogFactories.get(child.factory()));
                }
            }

            if (route.factory() != null && dialogFactories.containsKey(route.factory())) {
                 setField(route, "dialogFactoryInstance", dialogFactories.get(route.factory()));
            }

            // Handle Configuration (ItemFactory)
            RouteRendererConfiguration config = route.configuration();
            if (config != null) {
                try {
                    java.lang.reflect.Method factoryMethod = config.getClass().getMethod("factory");
                    Class<?> factoryClass = (Class<?>) factoryMethod.invoke(config);
                    if (factoryClass != null) {
                        setField(config, "factoryInstance", instantiate(factoryClass));
                    }
                } catch (Exception e) {
                    // Ignore
                }

                if (config.children() != null) {
                    for (InternalFormElement element : (List<InternalFormElement>) config.children()) {
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

                if (config instanceof MultiFormRendererConfiguration) {
                    MultiFormRendererConfiguration multiConfig = (MultiFormRendererConfiguration) config;
                    if (multiConfig.forms() != null) {
                        for (Object childFormObj : multiConfig.forms()) {
                             RouteRendererConfiguration childForm = (RouteRendererConfiguration) childFormObj;
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
                Map<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> children = ((RouteRendererMultipleChildren) route).childrenMap();
                if (children != null) {
                    traverseRoutes(children.values());
                }
            } else if (route instanceof RouteRendererSingleChild) {
                RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> child = ((RouteRendererSingleChild) route).child();
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
