package com.github.appreciated.turbo_crud.config.model;

import org.jsoup.nodes.FormElement;

import java.util.List;

public class FormConfiguration extends RouteConfiguration {
    private String titleField;

    private List<FormElement> children;

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }
}
