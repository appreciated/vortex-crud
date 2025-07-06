package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass>  extends RouteRendererConfiguration<DataStoreId, FieldId, ModelClass>  {

    private List<RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> > forms;

    public MultiFormRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public List<RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> > getForms() {
        return forms;
    }

    public void setForms(List<RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> > children) {
        this.forms = children;
    }


    public static class Builder<DataStoreId, FieldId, ModelClass>  {

        private final MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass>  product;

        private Builder(MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass>  product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId, ModelClass>  Builder<DataStoreId, FieldId, ModelClass>  of(Class<? extends VortexCrudItemFactory> factory) {
            return new Builder<>(new MultiFormRendererConfiguration<>((Class<? extends VortexCrudItemFactory<FieldId>>) factory));
        }

        public Builder<DataStoreId, FieldId, ModelClass>  withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass>  withForms(List<RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> > forms) {
            product.forms = forms;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass>  addForm(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass>  item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass>  build() {
            return product;
        }
    }
}
