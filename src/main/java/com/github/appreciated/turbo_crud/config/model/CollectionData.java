package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class CollectionData {
    String repository;
    @Optional
    private OneToManyConfiguration oneToMany;
    @Optional
    private ManyToManyConfiguration manyToMany;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public OneToManyConfiguration getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToManyConfiguration oneToMany) {
        this.oneToMany = oneToMany;
    }

    public ManyToManyConfiguration getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(ManyToManyConfiguration manyToMany) {
        this.manyToMany = manyToMany;
    }
}
