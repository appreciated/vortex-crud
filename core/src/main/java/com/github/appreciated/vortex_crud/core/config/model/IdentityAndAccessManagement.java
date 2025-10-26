package com.github.appreciated.vortex_crud.core.config.model;

import java.util.List;

public interface IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> {

    RepositoryType getRepositoryKey();

    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getSignUpFields();
}
