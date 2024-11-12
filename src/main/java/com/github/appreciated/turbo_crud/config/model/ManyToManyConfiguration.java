package com.github.appreciated.turbo_crud.config.model;

public class ManyToManyConfiguration {

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
}
