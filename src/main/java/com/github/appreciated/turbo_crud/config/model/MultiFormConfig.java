package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class MultiFormConfig {

    private String titleField;

    private List<FormConfiguration> children;

    public List<FormConfiguration> getChildren() {
        return children;
    }

    public void setChildren(List<FormConfiguration> children) {
        this.children = children;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }
}
