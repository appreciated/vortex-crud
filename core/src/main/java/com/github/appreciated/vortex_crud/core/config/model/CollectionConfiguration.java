package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionConfiguration<DataStoreId, FieldId> {

    private Class<? extends DataStoreId> dataStore;

    private OneToMany<DataStoreId, FieldId> oneToMany;

    private ManyToMany<DataStoreId, FieldId> manyToMany;

    private List<FieldId> children;

    public CollectionConfiguration(Class<? extends DataStoreId> dataStore) {
        this.dataStore = dataStore;
    }

    public Class<? extends DataStoreId> getDataStore() {
        return dataStore;
    }

    public void setDataStore(Class<? extends DataStoreId> dataStore) {
        this.dataStore = dataStore;
    }

    public OneToMany<DataStoreId, FieldId> getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany<DataStoreId, FieldId> oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany<DataStoreId, FieldId> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany<DataStoreId, FieldId> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<FieldId> getChildren() {
        return children;
    }

    public void setChildren(List<FieldId> children) {
        this.children = children;
    }

    public abstract static class Builder<DataStoreId, FieldId> {

        private final CollectionConfiguration<DataStoreId, FieldId> product;

        protected Builder(CollectionConfiguration<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId> withOneToMany(OneToMany<DataStoreId, FieldId> oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId> withManyToMany(ManyToMany<DataStoreId, FieldId> manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildren(List<FieldId> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildren(FieldId... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId> addChildren(FieldId item) {
            product.children.add(item);
            return this;
        }

        public CollectionConfiguration<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
