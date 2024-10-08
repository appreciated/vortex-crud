package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

public class RenderConfig {

    @Optional
    private boolean inlineEdit;
    @Optional
    private ItemRendererConfig itemRenderer;
    @Optional
    private DetailRenderer detailRenderer;

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }

    public ItemRendererConfig getItemRenderer() {
        return itemRenderer;
    }

    public void setItemRenderer(ItemRendererConfig itemRenderer) {
        this.itemRenderer = itemRenderer;
    }

    public DetailRenderer getDetailRenderer() {
        return detailRenderer;
    }

    public void setDetailRenderer(DetailRenderer detailRenderer) {
        this.detailRenderer = detailRenderer;
    }
}
