package com.github.appreciated.flow_cms.config.model;

import java.util.List;

public class DetailRenderer {
    private String type;
    private List<FormField> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FormField> getChildren() {
        return children;
    }

    public void setChildren(List<FormField> children) {
        this.children = children;
    }
}
