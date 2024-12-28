package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<DataStoreId, FieldId> extends RouteConfiguration<DataStoreId, FieldId> implements ItemFactory {

    public Kanban(Class<? extends TurboCrudItemFactory> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId> {

        private Kanban<DataStoreId, FieldId> product;

        private Builder(Kanban<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends TurboCrudItemFactory> factory) {
            return new Builder<>(new Kanban<>(factory));
        }

        public Builder<DataStoreId,FieldId> withColumnField(String columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withTitleField(String titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withDescriptionField(String descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageField(String imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageFactory(Class<? extends TurboCrudFileProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder<DataStoreId,FieldId> withChildren(List<InternalFormElement<DataStoreId,FieldId>> children) {
            product.setChildren(children);
            return this;
        }

        public Builder<DataStoreId,FieldId> addChildren(InternalFormElement<DataStoreId,FieldId> item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban<DataStoreId,FieldId> build() {
            return product;
        }
    }
}
