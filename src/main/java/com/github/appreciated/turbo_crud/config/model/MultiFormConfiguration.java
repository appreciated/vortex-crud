package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class MultiFormConfiguration extends RouteConfiguration {

    private List<RouteConfiguration> forms;

    public MultiFormConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public List<RouteConfiguration> getForms() {
        return forms;
    }

    public void setForms(List<RouteConfiguration> children) {
        this.forms = children;
    }


    public static class Builder {

        private MultiFormConfiguration product;

        private Builder(MultiFormConfiguration product) {
            this.product = product;
        }

        public static Builder of(Class<? extends TurboCrudItemFactory>  factory) {
            return new Builder(new MultiFormConfiguration(factory));
        }

        public Builder withTitleField(String titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder withForms(List<RouteConfiguration> forms) {
            product.forms = forms;
            return this;
        }

        public Builder addForm(RouteConfiguration item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormConfiguration build() {
            return product;
        }
    }
}
