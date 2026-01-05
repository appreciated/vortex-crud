package com.github.appreciated.vortex_crud.core.config.model;

import java.util.List;

public interface InternalFormElement<FieldType> extends ValidatableConfiguration {

    FieldType field();

    boolean readOnly();

    List<String> readOnlyForRoles();

    String label();

    int span();

}
