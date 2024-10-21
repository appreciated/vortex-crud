package com.github.appreciated.turbo_crud.ui.factories.route.grid.components;

import com.github.appreciated.turbo_crud.model.GenericEntity;

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
