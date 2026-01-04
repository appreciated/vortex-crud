package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.SignalAwareDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

public class DataStoreWrappingUtil {

    public static <ModelClass, FieldType, RepositoryType> void wrapDataStores(
            Application<ModelClass, FieldType, RepositoryType> application,
            SignalService signalService) {

        if (application == null) {
            return;
        }

        // Wrap NotificationPanel DataStore
        if (application.notificationPanelConfiguration() != null) {
            wrapDataStoreConfig(application.notificationPanelConfiguration().dataStoreConfig(), signalService);
        }

        // Wrap Routes DataStores
        Map<String, RouteRenderer<?, ?, ?>> routes = application.routes();
        if (routes != null) {
            for (RouteRenderer<?, ?, ?> route : routes.values()) {
                // We need to cast carefully or just use reflection since we know the structure
                wrapRouteDataStore(route, signalService);
            }
        }
    }

    private static void wrapRouteDataStore(RouteRenderer<?, ?, ?> route, SignalService signalService) {
        if (route.dataStoreConfig() != null) {
            wrapDataStoreConfig(route.dataStoreConfig(), signalService);
        }
    }

    private static void wrapDataStoreConfig(DataStoreConfig<?, ?, ?> config, SignalService signalService) {
        if (config == null || config.dataStoreInstance() == null) {
            return;
        }

        VortexCrudDataStore<?, ?> original = config.dataStoreInstance();

        // Avoid double wrapping
        if (original instanceof SignalAwareDataStore) {
            return;
        }

        // We need to create a new SignalAwareDataStore wrapping the original
        // But SignalAwareDataStore is generic.
        // We can cast because SignalAwareDataStore implements the same interface
        @SuppressWarnings("unchecked")
        SignalAwareDataStore wrapped = new SignalAwareDataStore(original, signalService);

        // Now we need to set this wrapped instance back into the config.
        @SuppressWarnings("unchecked")
        DataStoreConfig<Object, Object, Object> typedConfig = (DataStoreConfig<Object, Object, Object>) config;
        typedConfig.setDataStoreInstance(wrapped);
    }
}
