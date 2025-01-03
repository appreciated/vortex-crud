package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListConfiguration<DataStoreId,FieldId> extends RouteConfiguration<DataStoreId,FieldId> implements ItemFactory<FieldId> {

    private List<InternalFormElement<DataStoreId,FieldId>> children;

    public GridOrListConfiguration(Class<? extends TurboCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<InternalFormElement<DataStoreId,FieldId>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId,FieldId>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId,FieldId> extends RouteConfiguration.Builder<DataStoreId,FieldId> {

        private final GridOrListConfiguration<DataStoreId,FieldId> product;

        private Builder(GridOrListConfiguration<DataStoreId,FieldId> product) {
            super(product);
            this.product = product;
        }

        public static <DataStoreId,FieldId> Builder<DataStoreId,FieldId> of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder<>(new GridOrListConfiguration<>((Class<? extends TurboCrudItemFactory<FieldId>>)factory));
        }

        public Builder<DataStoreId,FieldId> withFilterField(FieldId filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder<DataStoreId,FieldId> withChildren(List<InternalFormElement<DataStoreId,FieldId>> children) {
            product.setChildren(children);
            return this;
        }

        public <T extends InternalFormElement<DataStoreId,FieldId>> Builder<DataStoreId,FieldId> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId,FieldId> addChildren(InternalFormElement<DataStoreId,FieldId> item) {
            product.children.add(item);
            return this;
        }

        public GridOrListConfiguration<DataStoreId,FieldId> build() {
            return product;
        }
    }
}
