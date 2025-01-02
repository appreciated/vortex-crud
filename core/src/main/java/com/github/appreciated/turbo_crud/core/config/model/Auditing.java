package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.List;

@GenerateBuilder
public class Auditing {

    private boolean enabled;

    private List<String> actions;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public static class Builder {

        private final Auditing product;

        private Builder(Auditing product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Auditing());
        }

        public Builder withEnabled(boolean enabled) {
            product.enabled = enabled;
            return this;
        }

        public Builder withActions(List<String> actions) {
            product.actions = actions;
            return this;
        }

        public Builder withActions(String... actions) {
            return withActions(List.of(actions));
        }

        public Builder addAction(String item) {
            product.actions.add(item);
            return this;
        }

        public Auditing build() {
            return product;
        }
    }
}
