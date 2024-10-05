package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class RenderConfig {
    @Optional
    private boolean inline_edit;
    @Optional
    private List<ColumnConfig> columns;
    @Optional
    private DetailRenderer detail_renderer;

    public boolean isInline_edit() {
        return inline_edit;
    }

    public void setInline_edit(boolean inline_edit) {
        this.inline_edit = inline_edit;
    }

    public List<ColumnConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnConfig> columns) {
        this.columns = columns;
    }

    public DetailRenderer getDetail_renderer() {
        return detail_renderer;
    }

    public void setDetail_renderer(DetailRenderer detail_renderer) {
        this.detail_renderer = detail_renderer;
    }
}
