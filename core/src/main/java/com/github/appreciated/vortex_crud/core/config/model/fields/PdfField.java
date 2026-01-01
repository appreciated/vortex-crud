package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.PdfFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Thin Field type for PdfFieldFactory.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PdfField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    List<Validator<?>> validators;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @Builder.Default
    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new PdfFieldFactory<>();

    private VortexCrudItemFactory<FieldType> itemFactory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;
}
