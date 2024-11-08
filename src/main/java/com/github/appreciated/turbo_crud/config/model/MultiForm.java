package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class MultiForm {

    private String titleField;

    private List<Form> children;

    public List<Form> getChildren() {
        return children;
    }

    public void setChildren(List<Form> children) {
        this.children = children;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }
}
