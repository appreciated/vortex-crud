package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;

@GenerateBuilder
public class Versioning {

    private boolean enabled;

    private List<String> repositories;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public static class Builder {

        private Versioning product;

        private Builder(Versioning product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Versioning());
        }

        public Builder withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder withRepositories(List<String> repositories) {
            product.repositories = repositories;
            return this;
        }

        public Builder addRepositorie(String item) {
            product.repositories.add(item);
            return this;
        }

        public Versioning build() {
            return product;
        }
    }
}
