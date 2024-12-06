package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import org.jsoup.nodes.FormElement;
import java.util.List;

@GenerateBuilder
public class Kanban extends RouteConfiguration implements ItemFactory {

    private Class<? extends TurboCrudItemFactory> factory;

    private String columnField;

    private String titleField;

    private String descriptionField;

    private String imageField;

    private String imageFactory;

    private List<FormElement> children;

    public Kanban(Class<? extends TurboCrudItemFactory>  factory) {
        super(factory);
    }

    public Class<? extends TurboCrudItemFactory> getFactory() {
        return factory;
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

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    public static class Builder {

        private Kanban product;

        private Builder(Kanban product) {
            this.product = product;
        }

        public static Builder of(String factory) {
            return new Builder(new Kanban(factory));
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public Builder withColumnField(String columnField) {
            product.columnField = columnField;
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

        public Builder withChildren(List<FormElement> children) {
            product.children = children;
            return this;
        }

        public Builder addChildren(FormElement item) {
            product.children.add(item);
            return this;
        }

        public Kanban build() {
            return product;
        }
    }
}
