package com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.converter;

import org.jooq.Converter;

public class IntegerBooleanConverter implements Converter<Integer, Boolean> {
    @Override
    public Boolean from(Integer databaseObject) {
        return databaseObject != null && databaseObject == 1;
    }

    @Override
    public Integer to(Boolean userObject) {
        return userObject != null && userObject ? 1 : 0;
    }

    @Override
    public Class<Integer> fromType() {
        return Integer.class;
    }

    @Override
    public Class<Boolean> toType() {
        return Boolean.class;
    }
}
