package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType> extends RouteRendererConfiguration<DataStoreId, FieldId, KeyType> {

    private List<RouteRendererConfiguration<DataStoreId, FieldId, KeyType>> forms;

    public MultiFormRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<RouteRendererConfiguration<DataStoreId, FieldId, KeyType>> getForms() {
        return forms;
    }

    public void setForms(List<RouteRendererConfiguration<DataStoreId, FieldId, KeyType>> children) {
        this.forms = children;
    }


    public static class Builder<DataStoreId, FieldId, KeyType> {

        private final MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType> product;

        private Builder(MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId, KeyType> Builder<DataStoreId, FieldId, KeyType> of(Class<? extends VortexCrudItemFactory> factory) {
            return new Builder<>(new MultiFormRendererConfiguration<>((Class<? extends VortexCrudItemFactory<FieldId>>) factory));
        }

        public Builder<DataStoreId, FieldId, KeyType> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withForms(List<RouteRendererConfiguration<DataStoreId, FieldId, KeyType>> forms) {
            product.forms = forms;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> addForm(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
