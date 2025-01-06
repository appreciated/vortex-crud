package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<DataStoreId, FieldId> extends RouteRendererConfiguration<DataStoreId, FieldId> implements ItemFactory<FieldId> {

    public Kanban(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public static class Builder<DataStoreId, FieldId> {

        private final Kanban<DataStoreId, FieldId> product;

        private Builder(Kanban<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(Class<? extends VortexCrudItemFactory> factory) {
            return new Builder<>(new Kanban<>(((Class<? extends VortexCrudItemFactory<FieldId>>)factory)));
        }

        public Builder<DataStoreId,FieldId> withColumnField(FieldId columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withDescriptionField(FieldId descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageField(FieldId imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<DataStoreId,FieldId> withImageFactory(Class<? extends VortexCrudFileProvider> imageFactory) {
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
