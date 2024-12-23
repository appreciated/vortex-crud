package com.github.appreciated.turbo_crud.core.config.model;

public class ManyToMany {

    private String associativeDataStore;

    private String associativeSourceIdField;

    private String associativeTargetIdField;

    private String dataStoreField;

    public ManyToMany(String associativeDataStore, String associativeSourceIdField, String associativeTargetIdField, String dataStoreField) {
        this.associativeDataStore = associativeDataStore;
        this.associativeSourceIdField = associativeSourceIdField;
        this.associativeTargetIdField = associativeTargetIdField;
        this.dataStoreField = dataStoreField;
    }

    public String getAssociativeDataStore() {
        return associativeDataStore;
    }

    public void setAssociativeDataStore(String associativeDataStore) {
        this.associativeDataStore = associativeDataStore;
    }

    public String getAssociativeSourceIdField() {
        return associativeSourceIdField;
    }

    public void setAssociativeSourceIdField(String associativeSourceIdField) {
        this.associativeSourceIdField = associativeSourceIdField;
    }

    public String getAssociativeTargetIdField() {
        return associativeTargetIdField;
    }

    public void setAssociativeTargetIdField(String associativeTargetIdField) {
        this.associativeTargetIdField = associativeTargetIdField;
    }

    public String getDataStoreField() {
        return dataStoreField;
    }

    public void setDataStoreField(String dataStoreField) {
        this.dataStoreField = dataStoreField;
    }

}
