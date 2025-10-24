package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    private List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> forms;

    public MultiFormRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }

    public List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> getForms() {
        return forms;
    }

    public void setForms(List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> children) {
        this.forms = children;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> product;

        private Builder(MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public static <ModelClass, FieldType, RepositoryType> Builder<ModelClass, FieldType, RepositoryType> of(Class<? extends VortexCrudItemFactory> factory) {
            return new Builder<>(new MultiFormRendererConfiguration<>((Class<? extends VortexCrudItemFactory<FieldType>>) factory));
        }

        public Builder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withForms(List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> forms) {
            product.forms = forms;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> addForm(RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
