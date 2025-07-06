package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<DataStoreId, FieldId, ModelClass> extends RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> implements ItemFactory<FieldId> {

    public Kanban(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public static abstract class Builder<DataStoreId, FieldId, ModelClass> {

        private final Kanban<DataStoreId, FieldId, ModelClass> product;

        protected Builder(Kanban<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withColumnField(FieldId columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withDescriptionField(FieldId descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withImageField(FieldId imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> withChildren(List<InternalFormElement<DataStoreId,FieldId, ModelClass>> children) {
            product.setChildren(children);
            return this;
        }

        public Builder<DataStoreId,FieldId, ModelClass> addChildren(InternalFormElement<DataStoreId,FieldId, ModelClass> item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban<DataStoreId,FieldId, ModelClass> build() {
            return product;
        }
    }
}
