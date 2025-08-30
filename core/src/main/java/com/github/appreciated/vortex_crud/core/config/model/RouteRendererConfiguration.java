package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class RouteRendererConfiguration<DataStoreId, FieldId, KeyType>
        implements RouteConfig<DataStoreId, FieldId, KeyType> {

    private Class<? extends VortexCrudItemFactory<FieldId>> factory;

    private FieldId titleField;

    private FieldId descriptionField;

    private FieldId imageField;

    private Class<? extends VortexCrudResourceProvider> imageFactory;

    private boolean inlineEdit;

    private FieldId filterField;

    private List<InternalFormElement<DataStoreId, FieldId, KeyType>> children;

    public RouteRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        this.factory = factory;
    }

    @Override
    public Class<? extends VortexCrudItemFactory<FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        this.factory = factory;
    }

    public FieldId getTitleField() {
        return titleField;
    }

    public void setTitleField(FieldId titleField) {
        this.titleField = titleField;
    }

    public FieldId getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(FieldId descriptionField) {
        this.descriptionField = descriptionField;
    }

    public FieldId getImageField() {
        return imageField;
    }

    public void setImageField(FieldId imageField) {
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

    public FieldId getFilterField() {
        return filterField;
    }

    public void setFilterField(FieldId filterField) {
        this.filterField = filterField;
    }

    public List<InternalFormElement<DataStoreId, FieldId, KeyType>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId, FieldId, KeyType>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId, FieldId, KeyType> {

        private final RouteRendererConfiguration<DataStoreId, FieldId, KeyType> product;

        public Builder(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withTitleField(FieldId titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withDescriptionField(FieldId descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withImageField(FieldId imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withFilterField(FieldId filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildren(List<InternalFormElement<DataStoreId, FieldId, KeyType>> children) {
            product.children = children;
            return this;
        }

        @SafeVarargs
        public final <T extends InternalFormElement<DataStoreId, FieldId, KeyType>> Builder<DataStoreId, FieldId, KeyType> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId, KeyType> addChildren(InternalFormElement<DataStoreId, FieldId, KeyType> item) {
            product.children.add(item);
            return this;
        }

        public RouteRendererConfiguration<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
