package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * Dialog factory that renders the form as a slide-in panel from the right.
 */
public class FormSlideFactory<DataStoreId, FieldId, KeyType>
        extends AbstractFormDialogFactory<DataStoreId, FieldId, KeyType> {

    public FormSlideFactory(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                            VortexCrudForeignKeyResolutionStrategy<FieldId> foreignKeyResolutionStrategy,
                            VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        super(configService, dataStoreFactoryRegistry, fieldNameResolver, foreignKeyResolutionStrategy, dataStoreUtil);
    }

    @Override
    protected Dialog instantiateDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.addClassName("form-slide-dialog");
        dialog.getElement().getStyle().set("--vaadin-dialog-overlay-width", "400px");
        dialog.getElement().getStyle().set("--vaadin-dialog-overlay-right", "0");
        dialog.getElement().getStyle().set("--vaadin-dialog-overlay-top", "0");
        dialog.getElement().getStyle().set("--vaadin-dialog-overlay-bottom", "0");
        dialog.getElement().getStyle().set("--vaadin-dialog-overlay-left", "auto");
        return dialog;
    }
}
