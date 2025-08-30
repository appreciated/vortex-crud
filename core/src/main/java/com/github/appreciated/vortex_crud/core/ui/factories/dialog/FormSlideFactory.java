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
        dialog.setCloseOnOutsideClick(true);
        dialog.setModal(false);
        dialog.setDraggable(false);
        dialog.addClassName("form-slide-dialog");
        dialog.setWidth("400px");
        dialog.setTop("0");
        dialog.setHeightFull();
        dialog.setLeft("calc(100% - 400px)");
        return dialog;
    }
}
