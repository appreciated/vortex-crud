package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class MultiFormRendererConfiguration<DataStoreId, FieldId> extends RouteRendererConfiguration<DataStoreId, FieldId> {

    private List<RouteRendererConfiguration<DataStoreId, FieldId>> forms;

    public MultiFormRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<RouteRendererConfiguration<DataStoreId, FieldId>> getForms() {
        return forms;
    }

    public void setForms(List<RouteRendererConfiguration<DataStoreId, FieldId>> children) {
        this.forms = children;
    }


    public static class Builder<DataStoreId, FieldId> {

        private final MultiFormRendererConfiguration<DataStoreId, FieldId> product;

        private Builder(MultiFormRendererConfiguration<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends VortexCrudItemFactory> factory) {
            return new Builder<>(new MultiFormRendererConfiguration<>((Class<? extends VortexCrudItemFactory<FieldId>>) factory));
        }

        public Builder<DataStoreId, FieldId> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId, FieldId> withForms(List<RouteRendererConfiguration<DataStoreId, FieldId>> forms) {
            product.forms = forms;
            return this;
        }

        public Builder<DataStoreId, FieldId> addForm(RouteRendererConfiguration<DataStoreId, FieldId> item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormRendererConfiguration<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
