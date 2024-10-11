package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class ItemFactoryConfig {

    @Optional
    private String type;
    @Optional
    private String titleColumn;
    @Optional
    private String descriptionColumn;
    @Optional
    private String imageColumn;
    @Optional
    private List<FormField> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<FormField> getChildren() {
        return children;
    }

    public void setChildren(List<FormField> children) {
        this.children = children;
    }
}
