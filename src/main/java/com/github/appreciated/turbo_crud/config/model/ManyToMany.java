package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ManyToMany {

    private String associativeRepository;

    private String associativeSourceIdField;

    private String associativeTargetIdField;

    private String repositoryField;

    public String getAssociativeRepository() {
        return associativeRepository;
    }

    public void setAssociativeRepository(String associativeRepository) {
        this.associativeRepository = associativeRepository;
    }

    public String getAssociativeSourceIdField() {
        return associativeSourceIdField;
    }

    public void setAssociativeSourceIdField(String associativeSourceIdField) {
        this.associativeSourceIdField = associativeSourceIdField;
    }

    public String getAssociativeTargetIdField() {
        return associativeTargetIdField;
    }

    public void setAssociativeTargetIdField(String associativeTargetIdField) {
        this.associativeTargetIdField = associativeTargetIdField;
    }

    public String getRepositoryField() {
        return repositoryField;
    }

    public void setRepositoryField(String repositoryField) {
        this.repositoryField = repositoryField;
    }

    public static class Builder {

        private ManyToMany product;

        private Builder(ManyToMany product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new ManyToMany());
        }

        public Builder withAssociativeRepository(String associativeRepository) {
            product.associativeRepository = associativeRepository;
            return this;
        }

        public Builder withAssociativeSourceIdField(String associativeSourceIdField) {
            product.associativeSourceIdField = associativeSourceIdField;
            return this;
        }

        public Builder withAssociativeTargetIdField(String associativeTargetIdField) {
            product.associativeTargetIdField = associativeTargetIdField;
            return this;
        }

        public Builder withRepositoryField(String repositoryField) {
            product.repositoryField = repositoryField;
            return this;
        }

        public ManyToMany build() {
            return product;
        }
    }
}
