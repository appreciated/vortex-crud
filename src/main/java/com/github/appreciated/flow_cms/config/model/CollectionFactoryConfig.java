package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class CollectionFactoryConfig {

    private String table;
    private String foreignKeyColumn;
    private List<FormField> children;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public List<FormField> getChildren() {
        return children;
    }

    public void setChildren(List<FormField> children) {
        this.children = children;
    }
}
