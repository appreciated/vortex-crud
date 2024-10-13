package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class FactoryConfig {

    @Optional
    private boolean inlineEdit;
    @Optional
    private ItemFactoryConfig itemFactory;
    @Optional
    private DetailFactory detailFactory;

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }

    public ItemFactoryConfig getItemFactory() {
        return itemFactory;
    }

    public void setItemFactory(ItemFactoryConfig itemFactory) {
        this.itemFactory = itemFactory;
    }

    public DetailFactory getDetailFactory() {
        return detailFactory;
    }

    public void setDetailFactory(DetailFactory detailFactory) {
        this.detailFactory = detailFactory;
    }
}
