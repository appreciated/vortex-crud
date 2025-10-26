package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>
        implements RouteConfig<FieldType> {

    private Class<? extends VortexCrudItemFactory<FieldType>> factory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private Class<? extends VortexCrudResourceProvider> imageFactory;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    public RouteRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        this.factory = factory;
    }

    @Override
    public Class<? extends VortexCrudItemFactory<FieldType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        this.factory = factory;
    }

    public FieldType getTitleField() {
        return titleField;
    }

    public void setTitleField(FieldType titleField) {
        this.titleField = titleField;
    }

    public FieldType getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(FieldType descriptionField) {
        this.descriptionField = descriptionField;
    }

    public FieldType getImageField() {
        return imageField;
    }

    public void setImageField(FieldType imageField) {
        this.imageField = imageField;
    }

    public Class<? extends VortexCrudResourceProvider> getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
        this.imageFactory = imageFactory;
    }

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }

    public FieldType getFilterField() {
        return filterField;
    }

    public void setFilterField(FieldType filterField) {
        this.filterField = filterField;
    }

    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
        this.children = children;
    }

    @Override
    public void setWriteRoles(List<String> writeRoles) {
        this.writeRoles = writeRoles;
    }

    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        this.readOnlyRoles = readOnlyRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> product;

        public Builder(RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            product.children = children;
            return this;
        }

        @SafeVarargs
        public final <T extends InternalFormElement<ModelClass, FieldType, RepositoryType>> Builder<ModelClass, FieldType, RepositoryType> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<ModelClass, FieldType, RepositoryType> addChildren(InternalFormElement<ModelClass, FieldType, RepositoryType> item) {
            product.children.add(item);
            return this;
        }

        public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
