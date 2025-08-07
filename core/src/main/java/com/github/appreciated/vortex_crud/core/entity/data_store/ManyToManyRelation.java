package com.github.appreciated.vortex_crud.core.entity.data_store;

public class ManyToManyRelation {

    private final String foreignKeyValue;
    private final String value;

    public ManyToManyRelation(String foreignKeyValue, String value) {
        this.foreignKeyValue = foreignKeyValue;
        this.value = value;
    }

    public String getForeignKeyValue() {
        return foreignKeyValue;
    }

    public String getValue() {
        return value;
    }
}
