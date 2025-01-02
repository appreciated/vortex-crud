package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class MultiFormConfiguration<DataStoreId, FieldId> extends RouteConfiguration<DataStoreId, FieldId> {

    private List<RouteConfiguration<DataStoreId, FieldId>> forms;

    public MultiFormConfiguration(Class<? extends TurboCrudItemFactory<?>> factory) {
        super(factory);
    }

    public List<RouteConfiguration<DataStoreId, FieldId>> getForms() {
        return forms;
    }

    public void setForms(List<RouteConfiguration<DataStoreId, FieldId>> children) {
        this.forms = children;
    }


    public static class Builder<DataStoreId, FieldId> {

        private final MultiFormConfiguration<DataStoreId, FieldId> product;

        private Builder(MultiFormConfiguration<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder<>(new MultiFormConfiguration<>((Class<? extends TurboCrudItemFactory<?>>) factory));
        }

        public Builder<DataStoreId, FieldId> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId, FieldId> withForms(List<RouteConfiguration<DataStoreId, FieldId>> forms) {
            product.forms = forms;
            return this;
        }

        public Builder<DataStoreId, FieldId> addForm(RouteConfiguration<DataStoreId, FieldId> item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormConfiguration<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
