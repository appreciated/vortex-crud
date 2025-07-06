package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> {

    private Class<? extends VortexCrudItemFactory<FieldId>> factory;

    private FieldId titleField;

    private FieldId descriptionField;

    private FieldId columnField;

    private FieldId imageField;

    private Class<? extends VortexCrudResourceProvider> imageFactory;

    private boolean inlineEdit;

    private FieldId filterField;

    private List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children;

    public RouteRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        this.factory = factory;
    }

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

    public FieldId getColumnField() {
        return columnField;
    }

    public void setColumnField(FieldId columnField) {
        this.columnField = columnField;
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

    public List<InternalFormElement<DataStoreId, FieldId, ModelClass>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId, FieldId, ModelClass> {

        private final RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> product;

        public Builder(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withTitleField(FieldId titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDescriptionField(FieldId descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withColumnField(FieldId columnField) {
            product.columnField = columnField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withImageField(FieldId imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withFilterField(FieldId filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildren(List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children) {
            product.children = children;
            return this;
        }

        @SafeVarargs
        public final <T extends InternalFormElement<DataStoreId, FieldId, ModelClass>> Builder<DataStoreId, FieldId, ModelClass> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId, ModelClass> addChildren(InternalFormElement<DataStoreId, FieldId, ModelClass> item) {
            product.children.add(item);
            return this;
        }

        public RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }
    }
}
