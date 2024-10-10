package com.github.appreciated.flow_cms.ui.factories.route.grid.components;

import com.github.appreciated.flow_cms.service.GenericEntity;

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
