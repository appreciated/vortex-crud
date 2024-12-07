package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class GridOrListConfiguration extends RouteConfiguration implements ItemFactory {

    private String titleField;

    private String descriptionField;

    private String imageField;

    private Class<? extends TurboCrudFileProvider> imageFactory;

    private String filterField;

    private boolean inlineEdit;

    private List<FormElement> children;

    public GridOrListConfiguration(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
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
    public Class<? extends TurboCrudFileProvider> getImageFactory() {
        return imageFactory;
    }

    public void setImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
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

        public Builder withChildren(FormElement ... children) {
            product.children = List.of(children);
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
