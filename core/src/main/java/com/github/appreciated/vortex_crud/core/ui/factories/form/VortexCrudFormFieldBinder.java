package com.github.appreciated.vortex_crud.core.ui.factories.form;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;

/**
 * Interface for binding entity fields to UI components.
 * 
 * @param <FieldId> The type of the field identifier
 */
public interface VortexCrudFormFieldBinder<FieldId> {

    Object extractEntityForField(GenericEntity entity1, FieldId field);

    void updateFieldInEntity(GenericEntity entity1, FieldId field, Object o);
}