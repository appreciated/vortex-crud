package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionConfiguration<DataStoreId, FieldId, KeyType> {

    private KeyType dataStore;

    private OneToMany<DataStoreId, FieldId, KeyType> oneToMany;

    private ManyToMany<DataStoreId, FieldId, KeyType> manyToMany;

    private List<FieldId> children;

    public CollectionConfiguration(KeyType dataStore) {
        this.dataStore = dataStore;
    }

    public KeyType getDataStore() {
        return dataStore;
    }

    public void setDataStore(KeyType dataStore) {
        this.dataStore = dataStore;
    }

    public OneToMany<DataStoreId, FieldId, KeyType> getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany<DataStoreId, FieldId, KeyType> oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany<DataStoreId, FieldId, KeyType> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany<DataStoreId, FieldId, KeyType> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<FieldId> getChildren() {
        return children;
    }

    public void setChildren(List<FieldId> children) {
        this.children = children;
    }

    public abstract static class Builder<DataStoreId, FieldId, KeyType> {

        private final CollectionConfiguration<DataStoreId, FieldId, KeyType> product;

        protected Builder(CollectionConfiguration<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withOneToMany(OneToMany<DataStoreId, FieldId, KeyType> oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withManyToMany(ManyToMany<DataStoreId, FieldId, KeyType> manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildren(List<FieldId> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildren(FieldId... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId, KeyType> addChildren(FieldId item) {
            product.children.add(item);
            return this;
        }

        public CollectionConfiguration<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
