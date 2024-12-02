package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class MultiFormConfiguration extends RouteConfiguration {

    private String titleField;

    private List<FormConfiguration> forms;

    public MultiFormConfiguration(String factory) {
        super(factory);
    }

    public List<FormConfiguration> getForms() {
        return forms;
    }

    public void setForms(List<FormConfiguration> children) {
        this.forms = children;
    }

    public String getTitleField() {
        return titleField;
    }

    public static class Builder {

        private MultiFormConfiguration product;

        private Builder(MultiFormConfiguration product) {
            this.product = product;
        }

        public static Builder of(String factory) {
            return new Builder(new MultiFormConfiguration(factory));
        }

        public Builder withTitleField(String titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder withForms(List<FormConfiguration> forms) {
            product.forms = forms;
            return this;
        }

        public Builder addForm(FormConfiguration item) {
            product.forms.add(item);
            return this;
        }

        public MultiFormConfiguration build() {
            return product;
        }
    }
}
