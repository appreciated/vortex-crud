package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.FileFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.vaadin.flow.data.binder.Validator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Thin Field type for FileFieldFactory.
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FileField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    List<Validator<?>> validators;
    boolean required;
    List<String> writeRoles;
    List<String> readOnlyRoles;
    @Builder.Default
    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory = new FileFieldFactory<>();

    private VortexCrudItemFactory<FieldType> itemFactory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    @Override
    public List<Validator<?>> validators() {
        return validators;
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public List<String> writeRoles() {
        return writeRoles;
    }

    @Override
    public List<String> readOnlyRoles() {
        return readOnlyRoles;
    }

    @Override
    public VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory() {
        return factory;
    }
}
