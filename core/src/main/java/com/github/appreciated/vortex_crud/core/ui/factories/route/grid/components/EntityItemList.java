package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import java.util.List;

public class EntityItemList<DataStoreId> {
    private final List<DataStoreId> list;

    public EntityItemList(List<DataStoreId> list) {
        this.list = list;
    }

    public List<DataStoreId> getList() {
        return list;
    }
}
