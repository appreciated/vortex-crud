package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

/**
 * Marker interface for factories creating form-based dialogs.
 * Implementations are expected to provide dialog instances for CRUD forms.
 */
public interface VortexCrudFormFactory<DataStoreId, FieldId, KeyType>
        extends VortexCrudDialogFactory<DataStoreId, FieldId, KeyType> {
}
