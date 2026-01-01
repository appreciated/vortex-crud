package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public class JpaDataStoreConfig {

    public static <T> Builder<T> builder(JpaRepository<T, ?> repository, JpaRepositoryDataStore<T> store) {
        return new Builder<>(repository, store);
    }

    public static class Builder<T> {
        private final JpaRepository<T, ?> repository;
        private final JpaRepositoryDataStore<T> store;
        private JpaFieldService fieldService;
        private Map<Class<?>, VortexCrudDataStore> storeMap;
        private Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> fields;

        public Builder(JpaRepository<T, ?> repository, JpaRepositoryDataStore<T> store) {
            this.repository = repository;
            this.store = store;
        }

        public Builder<T> withServices(
                JpaFieldService fieldService,
                Map<Class<?>, VortexCrudDataStore> storeMap) {
            this.fieldService = fieldService;
            this.storeMap = storeMap;
            return this;
        }

        public Builder<T> fields(Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> fields) {
            this.fields = fields;
            return this;
        }

        public DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build() {
            Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> finalFields;

            if (fields != null) {
                finalFields = fields;
            } else if (fieldService != null && storeMap != null) {
                finalFields = fieldService.getFieldsForDataStore(store, storeMap);
            } else {
                throw new IllegalStateException("Must provide fields manually or via services");
            }

            return DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .factory(repository)
                    .dataStoreInstance((VortexCrudDataStore) store)
                    .fields(finalFields)
                    .build();
        }
    }
}
