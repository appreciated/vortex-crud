package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class Kanban implements ItemFactory {

    private String factory;
    private String columnField;
    private String titleField;
    private String descriptionField;
    @Optional
    private String imageField;
    @Optional
    private String imageFactory;
    @Optional
    private List<FormElement> children;

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

    @Override
    public String getImageFactory() {
        return imageFactory;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }
}