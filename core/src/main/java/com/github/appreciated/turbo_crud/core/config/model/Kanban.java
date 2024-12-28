package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<DataStoreId> extends RouteConfiguration<DataStoreId> implements ItemFactory {

    public Kanban(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId> {

        private Kanban<DataStoreId> product;

        private Builder(Kanban<DataStoreId> product) {
            this.product = product;
        }

        public static <DataStoreId> Builder<DataStoreId> of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder<>(new Kanban<>(factory));
        }

        public Builder<DataStoreId> withColumnField(String columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<DataStoreId> withTitleField(String titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId> withDescriptionField(String descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<DataStoreId> withImageField(String imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<DataStoreId> withImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder<DataStoreId> withChildren(List<InternalFormElement<DataStoreId>> children) {
            product.setChildren(children);
            return this;
        }

        public Builder<DataStoreId> addChildren(InternalFormElement<DataStoreId> item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban<DataStoreId> build() {
            return product;
        }
    }
}
