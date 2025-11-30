package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestRegistry implements VortexCrudDataStoreFactoryRegistry<Object, String, String> {
    private final Map<String, VortexCrudDataStore<String, Object>> factories = new HashMap<>();

    @Override
    public VortexCrudDataStore<String, Object> getDataStore(String table) {
        return factories.get(table);
    }

    @Override
    public void addFactory(String table, VortexCrudDataStore<String, Object> factory) {
        factories.put(table, factory);
    }
}
