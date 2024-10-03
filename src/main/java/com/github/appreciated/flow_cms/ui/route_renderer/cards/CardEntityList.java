package com.github.appreciated.flow_cms.ui.route_renderer.cards;

import com.github.appreciated.flow_cms.service.GenericEntity;

import java.util.List;

public class CardEntityList {
    private final List<GenericEntity> list;

    public CardEntityList(List<GenericEntity> list) {
        this.list = list;
    }

    public List<GenericEntity> getList() {
        return list;
    }
}
