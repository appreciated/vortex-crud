package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record InternalFormElement<ModelClass, FieldType, RepositoryType>(
    FieldType field,
    Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean readOnly,
    List<String> readOnlyForRoles,
    String label,
    ViewFieldType type,
    int span,
    Collection<ModelClass, FieldType, RepositoryType> configuration
) {
    public InternalFormElement {
        // Auto-set span to 2 for collections if not explicitly set
        if (type == ViewFieldType.COLLECTION && span == 0) {
            span = 2;
        } else if (span == 0) {
            span = 1;
        }
    }

    // Explicit getters for backwards compatibility
    public FieldType getField() {
        return field;
    }

    public Class<? extends VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType>> factory() {
        return factory;
    }

    public String getLabel() {
        return label;
    }

    public ViewFieldType getType() {
        return type;
    }

    public int getSpan() {
        return span;
    }

    public Collection<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }
}