package com.github.appreciated.vortex_crud.core.entity.data_store;

public class ManyToManyRelation {

    private final String foreignKeyValue;
    private final Object value;

    public ManyToManyRelation(String foreignKeyValue, Object value) {
        this.foreignKeyValue = foreignKeyValue;
        this.value = value;
    }

    public String getForeignKeyValue() {
        return foreignKeyValue;
    }

    public Object getValue() {
        return value;
    }
}
