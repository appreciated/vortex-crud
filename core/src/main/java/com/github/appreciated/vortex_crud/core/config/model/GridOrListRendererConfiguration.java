package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListRendererConfiguration<DataStoreId,FieldId> extends RouteRendererConfiguration<DataStoreId,FieldId> implements ItemFactory<FieldId> {

    private List<InternalFormElement<DataStoreId,FieldId>> children;

    public GridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<InternalFormElement<DataStoreId,FieldId>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId,FieldId>> children) {
        this.children = children;
    }

    public static abstract class Builder<DataStoreId,FieldId> extends RouteRendererConfiguration.Builder<DataStoreId,FieldId> {

        private final GridOrListRendererConfiguration<DataStoreId,FieldId> product;

        protected Builder(GridOrListRendererConfiguration<DataStoreId, FieldId> product) {
            super(product);
            this.product = product;
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

        public Builder<DataStoreId,FieldId> addChildren(InternalFormElement<DataStoreId,FieldId> item) {
            product.children.add(item);
            return this;
        }

        public RouteRendererConfiguration<DataStoreId,FieldId> build() {
            return product;
        }
    }
}
