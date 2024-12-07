package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class GridOrListConfiguration extends RouteConfiguration implements ItemFactory {

    private List<FormElement> children;

    public GridOrListConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public static class Builder extends RouteConfiguration.Builder {

        private GridOrListConfiguration product;

        private Builder(GridOrListConfiguration product) {
            super(product);
            this.product = product;
        }

        public static Builder of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder(new GridOrListConfiguration(factory));
        }

        public Builder withFilterField(String filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder withInlineEdit(boolean inlineEdit) {
            product.setInlineEdit(inlineEdit);
            return this;
        }

        public Builder withChildren(List<FormElement> children) {
            product.setChildren(children);
            return this;
        }

        public Builder withChildren(FormElement ... children) {
            return withChildren(List.of(children));
        }

        public Builder addChildren(FormElement item) {
            product.children.add(item);
            return this;
        }

        public GridOrListConfiguration build() {
            return product;
        }
    }
}
