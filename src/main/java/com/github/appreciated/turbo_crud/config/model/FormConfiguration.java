package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class FormConfiguration {

    private String titleField;

    private List<FormItem> children;

    public List<FormItem> getChildren() {
        return children;
    }

    public void setChildren(List<FormItem> children) {
        this.children = children;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }
}
