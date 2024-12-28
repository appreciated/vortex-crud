package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListConfiguration<DataStoreId> extends RouteConfiguration<DataStoreId> implements ItemFactory {

    private List<InternalFormElement<DataStoreId>> children;

    public GridOrListConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public List<InternalFormElement<DataStoreId>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId>> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId> extends RouteConfiguration.Builder<DataStoreId> {

        private GridOrListConfiguration<DataStoreId> product;

        private Builder(GridOrListConfiguration<DataStoreId> product) {
            super(product);
            this.product = product;
        }

        public static <DataStoreId> Builder<DataStoreId> of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder<>(new GridOrListConfiguration<>(factory));
        }

        public Builder<DataStoreId> withFilterField(String filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<DataStoreId> withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder<DataStoreId> withChildren(List<InternalFormElement<DataStoreId>> children) {
            product.setChildren(children);
            return this;
        }

        public <T extends InternalFormElement<DataStoreId>> Builder<DataStoreId> withChildren(T... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId> addChildren(InternalFormElement<DataStoreId> item) {
            product.children.add(item);
            return this;
        }

        public GridOrListConfiguration<DataStoreId> build() {
            return product;
        }
    }
}
