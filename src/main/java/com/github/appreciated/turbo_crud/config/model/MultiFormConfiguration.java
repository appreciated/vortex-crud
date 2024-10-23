package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class MultiFormConfiguration {

    private String titleColumn;

    private List<FormConfiguration> children;

    public List<FormConfiguration> getChildren() {
        return children;
    }

    public void setChildren(List<FormConfiguration> children) {
        this.children = children;
    }

    public String getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(String titleColumn) {
        this.titleColumn = titleColumn;
    }
}
