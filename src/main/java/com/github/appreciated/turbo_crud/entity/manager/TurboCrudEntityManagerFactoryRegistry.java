package com.github.appreciated.turbo_crud.entity.manager;

public interface TurboCrudEntityManagerFactoryRegistry {
    TurboCrudEntityManagerService getFactory(String table);

    void addFactory(String table, TurboCrudEntityManagerService factory);
}
