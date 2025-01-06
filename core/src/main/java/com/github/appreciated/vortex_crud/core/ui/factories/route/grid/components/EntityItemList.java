package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;

import java.util.List;

public class EntityItemList {
    private final List<GenericEntity> list;

    public EntityItemList(List<GenericEntity> list) {
        this.list = list;
    }

    public List<GenericEntity> getList() {
        return list;
    }
}
