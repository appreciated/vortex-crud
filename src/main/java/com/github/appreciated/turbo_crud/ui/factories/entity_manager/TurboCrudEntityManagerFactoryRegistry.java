package com.github.appreciated.turbo_crud.ui.factories.entity_manager;

public interface TurboCrudEntityManagerFactoryRegistry {
    TurboCrudEntityManagerService getFactory(String table);

    void addFactory(String table, TurboCrudEntityManagerService factory);
}
