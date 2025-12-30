package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.config.visitor.I18nConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.config.visitor.I18nVisitable;
import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@Builder
@Getter
public class InternalFormElement<ModelClass, FieldType, RepositoryType> implements I18nVisitable {

    private FieldType field;

    private VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    @I18nKey
    private String label;

    private ViewFieldType type;

    private int span;

    private Collection<ModelClass, FieldType, RepositoryType> configuration;

    public InternalFormElement(FieldType field,
                               VortexCrudCollectionFactory<ModelClass, FieldType, RepositoryType> factory,
                               boolean readOnly,
                               List<String> readOnlyForRoles,
                               String label,
                               ViewFieldType type,
                               int span,
                               Collection<ModelClass, FieldType, RepositoryType> configuration) {
        this.field = field;
        this.factory = factory;
        this.readOnly = readOnly;
        this.readOnlyForRoles = readOnlyForRoles;
        this.label = label;
        this.type = type;
        this.configuration = configuration;
        // Auto-set span to 2 for collections if not explicitly set
        if (type == ViewFieldType.COLLECTION && span == 0) {
            this.span = 2;
        } else if (span == 0) {
            this.span = 1;
        } else {
            this.span = span;
        }
    }

    @Override
    public void accept(I18nConfigurationVisitor visitor) {
        visitor.visit(this);
        if (configuration != null) {
            if (configuration instanceof I18nVisitable) {
                ((I18nVisitable) configuration).accept(visitor);
            } else {
                visitor.visit(configuration);
            }
        }
    }
}
