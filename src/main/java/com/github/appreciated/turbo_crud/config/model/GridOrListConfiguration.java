package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class GridOrListConfiguration implements ItemFactory {

    private String factory;
    @Optional
    private String titleField;
    @Optional
    private String descriptionField;
    @Optional
    private String imageField;
    @Optional
    private String imageFactory;
    @Optional
    private String filterField;
    @Optional
    private boolean inlineEdit;
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

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }
}