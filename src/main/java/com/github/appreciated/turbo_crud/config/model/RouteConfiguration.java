package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class RouteConfiguration {

    private Class<? extends TurboCrudItemFactory> factory;

    private String titleField;

    private String descriptionField;

    private String columnField;

    private String imageField;

    private String imageFactory;

    private boolean inlineEdit;

    private String filterField;

    private List<FormElement> children;

    public RouteConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        this.factory = factory;
    }

    public Class<? extends TurboCrudItemFactory>  getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudItemFactory> factory) {
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

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    public String getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(String imageFactory) {
        this.imageFactory = imageFactory;
    }

    public boolean isInlineEdit() {
        return inlineEdit;
    }

    public void setInlineEdit(boolean inlineEdit) {
        this.inlineEdit = inlineEdit;
    }

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public List<FormElement> getChildren() {
        return children;
    }

    public void setChildren(List<FormElement> children) {
        this.children = children;
    }

    public static class Builder {

        private RouteConfiguration product;

        Builder(RouteConfiguration product) {
            this.product = product;
        }

        public static Builder of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder(new RouteConfiguration(factory));
        }

        public Builder withTitleField(String titleField) {
            product.titleField = titleField;
            return this;
        }

        public Builder withDescriptionField(String descriptionField) {
            product.descriptionField = descriptionField;
            return this;
        }

        public Builder withColumnField(String columnField) {
            product.columnField = columnField;
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

        public Builder withInlineEdit(boolean inlineEdit) {
            product.inlineEdit = inlineEdit;
            return this;
        }

        public Builder withFilterField(String filterField) {
            product.filterField = filterField;
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

        public RouteConfiguration build() {
            return product;
        }
    }
}
