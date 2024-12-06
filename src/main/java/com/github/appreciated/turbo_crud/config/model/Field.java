package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class Field {

    private  Class<? extends TurboCrudFieldFactory> factory;

    private boolean primary;

    private boolean required;

    private Validation validation;

    private String defaultValue;

    private String values;

    private String repository;

    private String field;

    private String filterField;

    private List<String> children;

    private List<String> readOnlyForRoles;

    RouteConfiguration configuration;

    public Field( Class<? extends TurboCrudFieldFactory> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("The factory must not be null");
        }
        this.factory = factory;
    }

    public Field( Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
        this(factory);
        this.primary = primary;
    }

    public Field( Class<? extends TurboCrudFieldFactory> factory, String values) {
        this(factory);
        this.values = values;
    }

    public Field( Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
        this(factory, primary);
        this.required = required;
    }

    public Field( Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
        this(factory, primary, required);
        this.validation = validation;
    }

    public Field( Class<? extends TurboCrudFieldFactory> factory, String field, String filterField, String repository, List<String> children) {
        this(factory);
        this.field = field;
        this.filterField = filterField;
        this.repository = repository;
        this.children = children;
    }

    public  Class<? extends TurboCrudFieldFactory> getFactory() {
        return factory;
    }

    public void setFactory( Class<? extends TurboCrudFieldFactory> factory) {
        this.factory = factory;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public RouteConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfiguration configuration) {
        this.configuration = configuration;
    }

    public static class Builder {

        private Field product;

        private Builder(Field product) {
            this.product = product;
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory) {
            return new Builder(new Field(factory));
        }

        public Builder withPrimary(boolean primary) {
            product.primary = primary;
            return this;
        }

        public Builder withRequired(boolean required) {
            product.required = required;
            return this;
        }

        public Builder withValidation(Validation validation) {
            product.validation = validation;
            return this;
        }

        public Builder withDefaultValue(String defaultValue) {
            product.defaultValue = defaultValue;
            return this;
        }

        public Builder withValues(String values) {
            product.values = values;
            return this;
        }

        public Builder withRepository(String repository) {
            product.repository = repository;
            return this;
        }

        public Builder withField(String field) {
            product.field = field;
            return this;
        }

        public Builder withFilterField(String filterField) {
            product.filterField = filterField;
            return this;
        }

        public Builder withChildren(List<String> children) {
            product.children = children;
            return this;
        }

        public Builder withReadOnlyForRoles(String ... readOnlyForRoles) {
            product.readOnlyForRoles = List.of(readOnlyForRoles);
            return this;
        }

        public Builder withConfiguration(RouteConfiguration configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder addChildren(String item) {
            product.children.add(item);
            return this;
        }

        public Builder addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public Builder add(boolean primary) {
            return withPrimary(primary);
        }

        public Builder add(boolean primary, boolean required) {
            return withPrimary(primary).withRequired(required);
        }

        public Field build() {
            return product;
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory, boolean primary) {
            return new Builder(new Field(factory, primary));
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory, String values) {
            return new Builder(new Field(factory, values));
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required) {
            return new Builder(new Field(factory, primary, required));
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory, boolean primary, boolean required, Validation validation) {
            return new Builder(new Field(factory, primary, required, validation));
        }

        public static Builder of( Class<? extends TurboCrudFieldFactory> factory, String field, String filterField, String repository, List<String> children) {
            return new Builder(new Field(factory, field, filterField, repository, children));
        }
    }
}
