package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class CollectionData {

    private String dataStore;

    private OneToMany oneToMany;

    private ManyToMany manyToMany;

    private List<String> children;

    public CollectionData(String dataStore) {
        this.dataStore = dataStore;
    }

    public String getDataStore() {
        return dataStore;
    }

    public void setDataStore(String dataStore) {
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

    public static class Builder {

        private CollectionData product;

        private Builder(CollectionData product) {
            this.product = product;
        }

        public static Builder of(String repository) {
            return new Builder(new CollectionData(repository));
        }

        public Builder withOneToMany(OneToMany oneToMany) {
            product.oneToMany = oneToMany;
            return this;
        }

        public Builder withManyToMany(ManyToMany manyToMany) {
            product.manyToMany = manyToMany;
            return this;
        }

        public Builder withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder withChildren(String... children) {
            return withChildren(List.of(children));
        }

        public Builder addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public CollectionData build() {
            return product;
        }
    }
}
