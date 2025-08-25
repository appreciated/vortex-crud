package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<DataStoreId, FieldId, KeyType> extends RouteRendererConfiguration<DataStoreId, FieldId, KeyType> implements ItemFactory<FieldId> {

    private FieldId columnField;

    public Kanban(Class<? extends VortexCrudItemFactory<FieldId>> factory) {
        super(factory);
    }

    public FieldId getColumnField() {
        return columnField;
    }

    public void setColumnField(FieldId columnField) {
        this.columnField = columnField;
    }

    public static abstract class Builder<DataStoreId, FieldId, KeyType> {

        private final Kanban<DataStoreId, FieldId, KeyType> product;

        protected Builder(Kanban<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withColumnField(FieldId columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withTitleField(FieldId titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withDescriptionField(FieldId descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withImageField(FieldId imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildren(List<InternalFormElement<DataStoreId, FieldId, KeyType>> children) {
            product.setChildren(children);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> addChildren(InternalFormElement<DataStoreId, FieldId, KeyType> item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
