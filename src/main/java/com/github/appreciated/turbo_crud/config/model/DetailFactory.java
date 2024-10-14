package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class DetailFactory {

    private String factory;
    private List<FormElement> children;
    public  String titleColumn;

    @Optional
    public  int span = 2;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

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

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }
}
