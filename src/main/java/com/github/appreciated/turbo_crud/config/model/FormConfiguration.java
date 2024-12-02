package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class FormConfiguration extends RouteConfiguration {

    private String titleField;

    private List<FormElement> children;

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public static class Builder {

        private FormConfiguration product;

        private Builder(FormConfiguration product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new FormConfiguration());
        }

        public Builder withTitleField(String titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder withChildren(List<FormElement> children) {
            product.children = children;
            return this;
        }

        public Builder addChildren(FormElement item) {
            product.children.add(item);
            return this;
        }

        public FormConfiguration build() {
            return product;
        }
    }
}
