package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class ItemRendererConfig {

    private String type;
    private String titleField;
    @Optional
    private String descriptionField;
    @Optional
    private String imageField;
    @Optional
    private List<FormField> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<FormField> getChildren() {
        return children;
    }

    public void setChildren(List<FormField> children) {
        this.children = children;
    }
}
