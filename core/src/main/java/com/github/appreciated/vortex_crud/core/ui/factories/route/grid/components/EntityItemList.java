package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import java.util.List;

public class EntityItemList<ModelClass> {
    private final List<ModelClass> list;

    public EntityItemList(List<ModelClass> list) {
        this.list = list;
    }

    public List<ModelClass> getList() {
        return list;
    }
}
