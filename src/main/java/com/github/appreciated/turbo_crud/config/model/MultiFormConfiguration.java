package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class MultiFormConfiguration extends RouteConfiguration {

    private String titleField;

    private List<FormConfiguration> forms;

    public List<FormConfiguration> getForms() {
        return forms;
    }

    public void setForms(List<FormConfiguration> children) {
        this.forms = children;
    }

    public String getTitleField() {
        return titleField;
    }
}