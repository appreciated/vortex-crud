package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JpaApplication extends Application<JpaRepository<?, ?>, String> {

    public static Builder of() {
        return new Builder(new JpaApplication());
    }

    public static class Builder extends Application.Builder<JpaRepository<?, ?>, String> {
        private Collection<JpaRepository<?, ?>> repositories;

        public Builder(Application<JpaRepository<?, ?>, String> application) {
            super(application);
        }

        @Override
        public Builder withDataStores(Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String>> dataStores) {
            throw new UnsupportedOperationException("For JpaApplication the datastore configuration will be generated automatically based on the repositories in the application context.");
        }

        @Override
        public Application<JpaRepository<?, ?>, String> build() {
            return super.build();
        }

        @Override
        public Builder withName(String name) {
            super.withName(name);
            return this;
        }

        @Override
        public Builder withI18nBundlePrefix(String i18nBundlePrefix) {
            super.withI18nBundlePrefix(i18nBundlePrefix);
            return this;
        }

        @Override
        public Builder withRoutes(LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String>> routes) {
            super.withRoutes(routes);
            return this;
        }

        @Override
        public Builder withUserManagement(UserManagement userManagement) {
            super.withUserManagement(userManagement);
            return this;
        }

        @Override
        public Builder withVersioning(Versioning<JpaRepository<?, ?>> versioning) {
            super.withVersioning(versioning);
            return this;
        }

        @Override
        public Builder withAuditing(Auditing auditing) {
            super.withAuditing(auditing);
            return this;
        }

        @Override
        public Builder withSelects(Selects selects) {
            super.withSelects(selects);
            return this;
        }
    }
}
