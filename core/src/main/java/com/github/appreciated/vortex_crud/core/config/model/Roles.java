package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Roles {

    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static class Builder {

        private final Roles product;

        private Builder(Roles product) {
            this.product = product;
        }

        public Builder withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public Roles build() {
            return product;
        }
    }

    public static Builder of() {
        return new Builder(new Roles());
    }
}
