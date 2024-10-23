package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class ItemConfig {

    private String factory;
    @Optional
    private String titleColumn;
    @Optional
    private String descriptionColumn;
    @Optional
    private String imageColumn;
    @Optional
    private boolean inlineEdit;
    @Optional
    private List<FormItem> children;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(String titleColumn) {
        this.titleColumn = titleColumn;
    }

    public String getDescriptionColumn() {
        return descriptionColumn;
    }

    public void setDescriptionColumn(String descriptionColumn) {
        this.descriptionColumn = descriptionColumn;
    }

    public String getImageColumn() {
        return imageColumn;
    }

    public void setImageColumn(String imageColumn) {
        this.imageColumn = imageColumn;
    }

    public List<FormItem> getChildren() {
        return children;
    }

    public void setChildren(List<FormItem> children) {
        this.children = children;
    }
}