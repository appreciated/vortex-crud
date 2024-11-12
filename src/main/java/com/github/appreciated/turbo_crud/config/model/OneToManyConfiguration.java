package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class OneToManyConfiguration {
    private String referenceField;

    public String getReferenceField() {
        return referenceField;
    }

    public void setReferenceField(String referenceField) {
        this.referenceField = referenceField;
    }
}
