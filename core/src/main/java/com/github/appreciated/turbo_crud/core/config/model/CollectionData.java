package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionData<DataStoreId, FieldId> {

    private DataStoreId dataStore;

    private OneToMany<FieldId> oneToMany;

    private ManyToMany<DataStoreId, FieldId> manyToMany;

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

    public OneToMany<FieldId>  getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany<FieldId>  oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany<DataStoreId, FieldId> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany<DataStoreId, FieldId> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public static class Builder<DataStoreId, FieldId> {

        private final CollectionData<DataStoreId, FieldId> product;

        private Builder(CollectionData<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public static <DataStoreId, FieldId> Builder<DataStoreId, FieldId> of(DataStoreId dataStore) {
            return new Builder<>(new CollectionData<>(dataStore));
        }

        public Builder<DataStoreId, FieldId> withOneToMany(OneToMany<FieldId> oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId> withManyToMany(ManyToMany<DataStoreId, FieldId> manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildren(String... children) {
            return withChildren(List.of(children));
        }

        public Builder<DataStoreId, FieldId> addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public CollectionData<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
