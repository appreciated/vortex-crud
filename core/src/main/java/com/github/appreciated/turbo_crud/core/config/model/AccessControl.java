package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class AccessControl {

    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static class Builder {

        private final AccessControl product;

        private Builder(AccessControl product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new AccessControl());
        }

        public Builder withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public AccessControl build() {
            return product;
        }
    }
}
