package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@SuperBuilder
@Getter
public class InternalFormElement<ModelClass, FieldType, RepositoryType> implements ValidatableConfiguration {

    @lombok.NonNull
    private FieldType field;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    @I18nKey
    @lombok.NonNull
    private String label;

    private ViewFieldType type;

    private int span;

}
