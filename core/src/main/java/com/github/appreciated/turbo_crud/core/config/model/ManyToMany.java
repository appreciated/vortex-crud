package com.github.appreciated.turbo_crud.core.config.model;

public class ManyToMany<DataStoreId, FieldId> {

    private DataStoreId associativeDataStore;

    private FieldId associativeSourceIdField;

    private FieldId associativeTargetIdField;

    private FieldId dataStoreField;

    public ManyToMany(DataStoreId associativeDataStore, FieldId associativeSourceIdField, FieldId associativeTargetIdField, FieldId dataStoreField) {
        this.associativeDataStore = associativeDataStore;
        this.associativeSourceIdField = associativeSourceIdField;
        this.associativeTargetIdField = associativeTargetIdField;
        this.dataStoreField = dataStoreField;
    }

    public DataStoreId getAssociativeDataStore() {
        return associativeDataStore;
    }

    public void setAssociativeDataStore(DataStoreId associativeDataStore) {
        this.associativeDataStore = associativeDataStore;
    }

    public FieldId getAssociativeSourceIdField() {
        return associativeSourceIdField;
    }

    public void setAssociativeSourceIdField(FieldId associativeSourceIdField) {
        this.associativeSourceIdField = associativeSourceIdField;
    }

    public FieldId getAssociativeTargetIdField() {
        return associativeTargetIdField;
    }

    public void setAssociativeTargetIdField(FieldId associativeTargetIdField) {
        this.associativeTargetIdField = associativeTargetIdField;
    }

    public FieldId getDataStoreField() {
        return dataStoreField;
    }

    public void setDataStoreField(FieldId dataStoreField) {
        this.dataStoreField = dataStoreField;
    }

}
