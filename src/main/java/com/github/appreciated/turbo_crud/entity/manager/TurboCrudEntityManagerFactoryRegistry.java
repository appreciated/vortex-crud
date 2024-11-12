package com.github.appreciated.turbo_crud.entity.manager;

public interface TurboCrudEntityManagerFactoryRegistry {
    TurboCrudEntityManager getFactory(String table);

    void addFactory(String table, TurboCrudEntityManager factory);
}
