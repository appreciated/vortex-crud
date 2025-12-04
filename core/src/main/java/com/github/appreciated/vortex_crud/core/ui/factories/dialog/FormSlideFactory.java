package com.github.appreciated.vortex_crud.core.ui.factories.dialog;

import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.dialog.Dialog;

/**
 * Dialog factory that renders the form as a slide-in panel from the right.
 */
public class FormSlideFactory<ModelClass, FieldType, RepositoryType>
        extends AbstractFormDialogFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    protected Dialog instantiateDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        dialog.setModality(ModalityMode.VISUAL);
        dialog.setDraggable(false);
        dialog.addClassName("form-slide-dialog");
        dialog.setWidth("400px");
        dialog.setTop("0");
        dialog.setHeightFull();
        dialog.setLeft("calc(100% - 400px)");
        return dialog;
    }
}
