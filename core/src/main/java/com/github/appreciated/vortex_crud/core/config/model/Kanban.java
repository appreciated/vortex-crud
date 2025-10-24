package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Kanban<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> implements ItemFactory<FieldType> {

    private FieldType columnField;
    private FieldType rowIndexField;

    public Kanban(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }

    public FieldType getColumnField() {
        return columnField;
    }

    public void setColumnField(FieldType columnField) {
        this.columnField = columnField;
    }

    public FieldType getRowIndexField() {
        return rowIndexField;
    }

    public void setRowIndexField(FieldType rowIndexField) {
        this.rowIndexField = rowIndexField;
    }

    public static abstract class Builder<ModelClass, FieldType, RepositoryType> {

        private final Kanban<ModelClass, FieldType, RepositoryType> product;

        protected Builder(Kanban<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withColumnField(FieldType columnField) {
            product.setColumnField(columnField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withRowIndexField(FieldType rowIndexField) {
            product.setRowIndexField(rowIndexField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withTitleField(FieldType titleField) {
            product.setTitleField(titleField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withDescriptionField(FieldType descriptionField) {
            product.setDescriptionField(descriptionField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withImageField(FieldType imageField) {
            product.setImageField(imageField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withFilterField(FieldType filterField) {
            product.setFilterField(filterField);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withImageFactory(Class<? extends VortexCrudResourceProvider> imageFactory) {
            product.setImageFactory(imageFactory);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildren(List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children) {
            product.setChildren(children);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> addChildren(InternalFormElement<ModelClass, FieldType, RepositoryType> item) {
            product.getChildren().add(item);
            return this;
        }

        public Kanban<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
