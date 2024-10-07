package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

public class ItemRendererConfig {

    private String type;
    private String title_field;
    @Optional
    private String description_field;
    @Optional
    private String image_field;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle_field() {
        return title_field;
    }

    public void setTitle_field(String title_field) {
        this.title_field = title_field;
    }

    public String getDescription_field() {
        return description_field;
    }

    public void setDescription_field(String description_field) {
        this.description_field = description_field;
    }

    public String getImage_field() {
        return image_field;
    }

    public void setImage_field(String image_field) {
        this.image_field = image_field;
    }
}
