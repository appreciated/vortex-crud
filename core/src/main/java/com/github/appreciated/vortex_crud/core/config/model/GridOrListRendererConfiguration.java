package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListRendererConfiguration<DataStoreId, FieldId, KeyType> extends RouteRendererConfiguration<DataStoreId, FieldId, KeyType> implements ItemFactory<FieldId> {

    private List<InternalFormElement<DataStoreId, FieldId, KeyType>> children;

    public GridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<InternalFormElement<DataStoreId, FieldId, KeyType>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId, FieldId, KeyType>> children) {
        this.children = children;
    }

    public static abstract class Builder<DataStoreId, FieldId, KeyType> extends RouteRendererConfiguration.Builder<DataStoreId, FieldId, KeyType> {

        private final GridOrListRendererConfiguration<DataStoreId, FieldId, KeyType> product;

        protected Builder(GridOrListRendererConfiguration<DataStoreId, FieldId, KeyType> product) {
            super(product);
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withFilterField(FieldId filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildren(List<InternalFormElement<DataStoreId, FieldId, KeyType>> children) {
            product.setChildren(children);
            return this;
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
