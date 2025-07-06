package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListRendererConfiguration<DataStoreId, FieldId, ModelClass> extends RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> implements ItemFactory<FieldId> {

    private List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children;

    public GridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<InternalFormElement<DataStoreId, FieldId, ModelClass>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children) {
        this.children = children;
    }

    public static abstract class Builder<DataStoreId, FieldId, ModelClass> extends RouteRendererConfiguration.Builder<DataStoreId, FieldId, ModelClass> {

        private final GridOrListRendererConfiguration<DataStoreId, FieldId, ModelClass> product;

        protected Builder(GridOrListRendererConfiguration<DataStoreId, FieldId, ModelClass> product) {
            super(product);
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withFilterField(FieldId filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildren(List<InternalFormElement<DataStoreId, FieldId, ModelClass>> children) {
            product.setChildren(children);
            return this;
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
