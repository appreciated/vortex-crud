package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.TurboCrudCollectionFactory;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class FormElement {

    private String field;

    private Class<? extends TurboCrudCollectionFactory> factory;

    private boolean readOnly;

    private List<String> readOnlyForRoles;

    private String label;

    private String type;

    private Integer span = null;

    Collection configuration;

    public FormElement() {
    }

    public FormElement(String field, String type, String label) {
        this.field = field;
        this.type = type;
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Class<? extends TurboCrudCollectionFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudCollectionFactory> factory) {
        this.factory = factory;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<String> getReadOnlyForRoles() {
        return readOnlyForRoles;
    }

    public void setReadOnlyForRoles(List<String> readOnlyForRoles) {
        this.readOnlyForRoles = readOnlyForRoles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public Collection getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection configuration) {
        this.configuration = configuration;
    }

    public static class Builder {

        private FormElement product;

        private Builder(FormElement product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new FormElement());
        }

        public Builder withField(String field) {
            product.field = field;
            return this;
        }

        public Builder withFactory(Class<? extends TurboCrudCollectionFactory> factory) {
            product.factory = factory;
            return this;
        }

        public Builder withReadOnly(boolean readOnly) {
            product.readOnly = readOnly;
            return this;
        }

        public Builder withReadOnlyForRoles(List<String> readOnlyForRoles) {
            product.readOnlyForRoles = readOnlyForRoles;
            return this;
        }

        public Builder withLabel(String label) {
            product.label = label;
            return this;
        }

        public Builder withType(String type) {
            product.type = type;
            return this;
        }

        public Builder withSpan(Integer span) {
            product.span = span;
            return this;
        }

        public Builder withConfiguration(Collection configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder addReadOnlyForRole(String item) {
            product.readOnlyForRoles.add(item);
            return this;
        }

        public FormElement build() {
            return product;
        }

        public static Builder of(String field, String type, String label) {
            return new Builder(new FormElement(field, type, label));
        }
    }
}
