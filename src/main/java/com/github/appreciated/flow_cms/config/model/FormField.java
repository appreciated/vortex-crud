package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class FormField {
    private String type;
    private String field;
    @Optional
    private boolean read_only;
    @Optional
    private List<String> read_only_for_roles;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isRead_only() {
        return read_only;
    }

    public void setRead_only(boolean read_only) {
        this.read_only = read_only;
    }

    public List<String> getRead_only_for_roles() {
        return read_only_for_roles;
    }

    public void setRead_only_for_roles(List<String> read_only_for_roles) {
        this.read_only_for_roles = read_only_for_roles;
    }
}
