package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class RouteConfiguration<DataStoreId> {

    private Class<? extends TurboCrudItemFactory> factory;

    private String titleField;

    private String descriptionField;

    private String columnField;

    private String imageField;

    private Class<? extends TurboCrudFileProvider> imageFactory;

    private boolean inlineEdit;

    private String filterField;

    private List<InternalFormElement<DataStoreId>> children;

    public RouteConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudItemFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudItemFactory> factory) {
        this.factory = factory;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
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

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public List<InternalFormElement<DataStoreId>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId> {

        private RouteConfiguration<DataStoreId> product;

        public Builder(RouteConfiguration<DataStoreId> product) {
            this.product = product;
        }

        public Builder<DataStoreId> withTitleField(String titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder<DataStoreId> withDescriptionField(String descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder<DataStoreId> withColumnField(String columnField) {
            product.columnField = columnField;
            return this;
        }

        public Builder<DataStoreId> withImageField(String imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder<DataStoreId> withImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder<DataStoreId> withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder<DataStoreId> withFilterField(String filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder<DataStoreId> withChildren(List<InternalFormElement<DataStoreId>> children) {
            product.children = children;
            return this;
        }

        public <T extends InternalFormElement<DataStoreId>> Builder<DataStoreId> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId> addChildren(InternalFormElement<DataStoreId> item) {
            product.children.add(item);
            return this;
        }

        public RouteConfiguration<DataStoreId> build() {
            return product;
        }
    }
}
