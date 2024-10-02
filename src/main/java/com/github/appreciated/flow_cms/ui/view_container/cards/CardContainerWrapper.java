package com.github.appreciated.flow_cms.ui.view_container.cards;

import com.github.appreciated.flow_cms.service.GenericEntity;

import java.util.ArrayList;
import java.util.List;

public class CardContainerWrapper {
    private final List<GenericEntity> list;

    public CardContainerWrapper(List<GenericEntity> list) {
        this.list = list;
    }

    public List<GenericEntity> getList() {
        return list;
    }
}
