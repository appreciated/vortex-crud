package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class FormConfiguration {

    private String titleColumn;

    private List<FormElement> children;

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public String getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(String titleColumn) {
        this.titleColumn = titleColumn;
    }
}
