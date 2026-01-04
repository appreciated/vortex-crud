package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Accessors(fluent = true)
@AllArgsConstructor
@SuperBuilder
@Getter
public class FormElement<ModelClass, FieldType, RepositoryType> implements ValidatableConfiguration, InternalFormElement<FieldType> {

    @lombok.NonNull
    private FieldType field;

    private VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    @I18nKey
    @lombok.NonNull
    private String label;

    private int span;
}