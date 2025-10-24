package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionConfiguration<ModelClass, FieldType, RepositoryType> {

    private RepositoryType dataStore;

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;

    public CollectionConfiguration(RepositoryType dataStore) {
        this.dataStore = dataStore;
    }

    public RepositoryType getDataStore() {
        return dataStore;
    }

    public void setDataStore(RepositoryType dataStore) {
        this.dataStore = dataStore;
    }

    public OneToMany<ModelClass, FieldType, RepositoryType> getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany<ModelClass, FieldType, RepositoryType> oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToMany<ModelClass, FieldType, RepositoryType> getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany) {
        this.manyToMany = manyToMany;
    }

    public List<FieldType> getChildren() {
        return children;
    }

    public void setChildren(List<FieldType> children) {
        this.children = children;
    }

    public abstract static class Builder<ModelClass, FieldType, RepositoryType> {

        private final CollectionConfiguration<ModelClass, FieldType, RepositoryType> product;

        protected Builder(CollectionConfiguration<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withOneToMany(OneToMany<ModelClass, FieldType, RepositoryType> oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withManyToMany(ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildren(List<FieldType> children) {
            product.children = children;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildren(FieldType... children) {
            return withChildren(List.of(children));
        }

        public Builder<ModelClass, FieldType, RepositoryType> addChildren(FieldType item) {
            product.children.add(item);
            return this;
        }

        public CollectionConfiguration<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
