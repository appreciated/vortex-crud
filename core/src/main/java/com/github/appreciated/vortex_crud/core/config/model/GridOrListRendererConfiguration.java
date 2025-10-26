package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> implements ItemFactory<FieldType> {

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    public GridOrListRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }

    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> getChildren() {
        return children;
    }

    public void setChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
        this.children = children;
    }

    public static abstract class Builder<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration.Builder<ModelClass, FieldType, RepositoryType> {

        private final GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> product;

        protected Builder(GridOrListRendererConfiguration<ModelClass, FieldType, RepositoryType> product) {
            super(product);
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            product.setChildren(children);
            return this;
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
