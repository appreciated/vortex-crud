package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionData<DataStoreId> {

    private DataStoreId dataStore;

    private OneToMany oneToMany;

    private ManyToMany manyToMany;

    private List<String> children;

    public CollectionData(DataStoreId dataStore) {
        this.dataStore = dataStore;
    }

    public DataStoreId getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStoreId dataStore) {
        this.dataStore = dataStore;
    }

    public OneToMany getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId> {

        private CollectionData<DataStoreId> product;

        private Builder(CollectionData<DataStoreId> product) {
            this.product = product;
        }

        public static <DataStoreId> Builder<DataStoreId> of(DataStoreId dataStore) {
            return new Builder<>(new CollectionData<>(dataStore));
        }

        public Builder<DataStoreId> withOneToMany(OneToMany oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<DataStoreId> withManyToMany(ManyToMany manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<DataStoreId> withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId> withChildren(String... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId> addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public CollectionData<DataStoreId> build() {
            return product;
        }
    }
}
