package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban extends RouteConfiguration implements ItemFactory {

    public Kanban(Class<? extends TurboCrudItemFactory>  factory) {
        super(factory);
    }

    public static class Builder {

        private Kanban product;

        private Builder(Kanban product) {
            this.product = product;
        }

        public static Builder of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder(new Kanban(factory));
        }

        public Builder withColumnField(String columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder withTitleField(String titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder withDescriptionField(String descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder withImageField(String imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder withImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder withChildren(List<FormElement> children) {
            product.setChildren(children);
            return this;
        }

        public Builder addChildren(FormElement item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban build() {
            return product;
        }
    }
}
