package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class FormElement {

    @Optional
    private String column;
    @Optional
    private String factory;
    @Optional
    private boolean readOnly;
    @Optional
    private List<String> readOnlyForRoles;
    @Optional
    private String label;
    @Optional
    private String table;
    private String type;
    @Optional
    private String foreignKeyColumn;
    @Optional
    private String emptyMessage;
    @Optional
    private Integer span = null;
    @Optional
    DialogConfig dialog;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<String> getReadOnlyForRoles() {
        return readOnlyForRoles;
    }

    public void setReadOnlyForRoles(List<String> readOnlyForRoles) {
        this.readOnlyForRoles = readOnlyForRoles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public DialogConfig getDialog() {
        return dialog;
    }

    public void setDialog(DialogConfig dialog) {
        this.dialog = dialog;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }
}
