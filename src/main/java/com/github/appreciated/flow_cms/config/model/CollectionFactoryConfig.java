package com.github.appreciated.flow_cms.config.model;

import java.util.List;

public class CollectionFactoryConfig {

    private String table;
    private String label;
    private String foreignKeyColumn;
    private String dialogFactory;
    private String emptyMessage;
    private List<FormField> children;
    private DetailFactory detailFactory;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDialogFactory() {
        return dialogFactory;
    }

    public void setDialogFactory(String dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    public DetailFactory getDetailFactory() {
        return detailFactory;
    }

    public void setDetailFactory(DetailFactory detailFactory) {
        this.detailFactory = detailFactory;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }
}
