package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class GridOrListConfiguration extends RouteConfiguration implements ItemFactory {

    private String factory;

    private String titleField;

    private String descriptionField;

    private String imageField;

    private String imageFactory;

    private String filterField;

    private boolean inlineEdit;

    private List<FormElement> children;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTitleField() {
        return titleField;
    }

    public void setTitleField(String titleField) {
        this.titleField = titleField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String descriptionField) {
        this.descriptionField = descriptionField;
    }

    public String getImageField() {
        return imageField;
    }

    @Override
    public String getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(String imageFactory) {
        this.imageFactory = imageFactory;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public static class Builder {

        private GridOrListConfiguration product;

        private Builder(GridOrListConfiguration product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new GridOrListConfiguration());
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public Builder withTitleField(String titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder withDescriptionField(String descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder withImageField(String imageField) {
            product.imageField = imageField;
            return this;
        }

        public Builder withImageFactory(String imageFactory) {
            product.imageFactory = imageFactory;
            return this;
        }

        public Builder withFilterField(String filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
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

        public GridOrListConfiguration build() {
            return product;
        }
    }
}
