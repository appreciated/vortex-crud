package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class GridConfig implements ItemFactoryConfig{

    private String factory;
    @Optional
    private String titleField;
    @Optional
    private String descriptionField;
    @Optional
    private String imageField;
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

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    public List<FormItem> getChildren() {
        return children;
    }

    public void setChildren(List<FormItem> children) {
        this.children = children;
    }
}