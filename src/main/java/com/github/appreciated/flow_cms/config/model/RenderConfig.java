package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

public class RenderConfig {
    @Optional
    private boolean inline_edit;
    @Optional
    private CardRendererConfig card_renderer;
    @Optional
    private DetailRenderer detail_renderer;

    public boolean isInline_edit() {
        return inline_edit;
    }

    public void setInline_edit(boolean inline_edit) {
        this.inline_edit = inline_edit;
    }

    public CardRendererConfig getCard_renderer() {
        return card_renderer;
    }

    public void setCard_renderer(CardRendererConfig card_renderer) {
        this.card_renderer = card_renderer;
    }

    public DetailRenderer getDetail_renderer() {
        return detail_renderer;
    }

    public void setDetail_renderer(DetailRenderer detail_renderer) {
        this.detail_renderer = detail_renderer;
    }
}
