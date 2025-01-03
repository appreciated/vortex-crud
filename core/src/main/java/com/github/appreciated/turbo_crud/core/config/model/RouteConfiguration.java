package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class RouteConfiguration<DataStoreId, FieldId> {

    private Class<? extends TurboCrudItemFactory<FieldId>> factory;

    private FieldId titleField;

    private FieldId descriptionField;

    private FieldId columnField;

    private FieldId imageField;

    private Class<? extends TurboCrudFileProvider> imageFactory;

    private boolean inlineEdit;

    private FieldId filterField;

    private List<InternalFormElement<DataStoreId, FieldId>> children;

    public RouteConfiguration(Class<? extends TurboCrudItemFactory<FieldId>> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudItemFactory<FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudItemFactory<FieldId>> factory) {
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

    public Class<? extends TurboCrudFileProvider> getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
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

    public List<InternalFormElement<DataStoreId, FieldId>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId, FieldId>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final RouteConfiguration<DataStoreId, FieldId> product;

        public Builder(RouteConfiguration<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public Builder<DataStoreId,FieldId> withTitleField(FieldId titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder<DataStoreId,FieldId> withDescriptionField(FieldId descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder<DataStoreId,FieldId> withColumnField(FieldId columnField) {
            product.columnField = columnField;
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageField(FieldId imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder<DataStoreId,FieldId> withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder<DataStoreId,FieldId> withFilterField(FieldId filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId,FieldId> withChildren(List<InternalFormElement<DataStoreId, FieldId>> children) {
            product.children = children;
            return this;
        }

        @SafeVarargs
        public final <T extends InternalFormElement<DataStoreId, FieldId>> Builder<DataStoreId,FieldId> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId,FieldId> addChildren(InternalFormElement<DataStoreId, FieldId> item) {
            product.children.add(item);
            return this;
        }

        public RouteConfiguration<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
