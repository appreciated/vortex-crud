package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class GridOrListConfiguration extends RouteConfiguration implements ItemFactory {

    private String factory;
    private String titleField;
    private String descriptionField;
    private String imageField;
    private String imageFactory;
    private String filterField;
    private boolean inlineEdit;
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

    public void setImageFactory(String imageFactory) {
        this.imageFactory = imageFactory;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
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

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }
}