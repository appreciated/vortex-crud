package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionConfiguration<DataStoreId, FieldId, ModelClass> {

    private DataStoreId dataStore;

    private OneToMany<DataStoreId, FieldId, ModelClass> oneToMany;

    private ManyToMany<DataStoreId, FieldId, ModelClass> manyToMany;

    private List<String> children;

    public CollectionConfiguration(DataStoreId dataStore) {
        this.dataStore = dataStore;
    }

    public DataStoreId getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStoreId dataStore) {
        this.dataStore = dataStore;
    }

    public OneToMany<DataStoreId, FieldId, ModelClass> getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany<DataStoreId, FieldId, ModelClass> oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany<DataStoreId, FieldId, ModelClass> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany<DataStoreId, FieldId, ModelClass> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public abstract static class Builder<DataStoreId, FieldId, ModelClass> {

        private final CollectionConfiguration<DataStoreId, FieldId, ModelClass> product;

        protected Builder(CollectionConfiguration<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withOneToMany(OneToMany<DataStoreId, FieldId, ModelClass> oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withManyToMany(ManyToMany<DataStoreId, FieldId, ModelClass> manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildren(String... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId, ModelClass> addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public CollectionConfiguration<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }
    }
}
