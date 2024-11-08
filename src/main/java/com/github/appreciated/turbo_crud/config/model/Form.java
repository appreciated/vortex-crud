package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class Form {

    private String titleField;

    private List<FormElement> children;

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }
}
