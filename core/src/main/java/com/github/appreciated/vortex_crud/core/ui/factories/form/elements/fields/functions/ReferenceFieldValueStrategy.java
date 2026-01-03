package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

/**
 * Strategy for handling reference field values in forms.
 * Different ORM implementations may require different value types:
 * - JPA: Uses entity objects for foreign key fields
 * - jOOQ: Uses ID values for foreign key fields
 */
public interface ReferenceFieldValueStrategy {

    /**
     * Prepares a value to be returned from a reference field component.
     *
     * @param entityValue The entity object from the combobox
     * @param idValue The extracted ID value
     * @param dataStoreUtil Utility for extracting IDs from entities
     * @return The value to be set on the entity field (entity object for JPA, ID for jOOQ)
     */
    Object prepareValueForEntity(Object entityValue, Object idValue, VortexCrudDataStoreUtilStrategy dataStoreUtil);

    /**
     * Processes an incoming value for the setValue() method.
     * Extracts the ID, loads/reloads the entity as needed, and returns both values.
     *
     * @param value The incoming value (could be an entity or an ID)
     * @param dataStore The data store to load entities from
     * @param dataStoreUtil Utility for extracting IDs from entities
     * @return A ValueHolder containing both the entity and ID values
     */
    ValueHolder processIncomingValue(Object value, VortexCrudDataStore<?, ?> dataStore, VortexCrudDataStoreUtilStrategy dataStoreUtil);

    /**
     * Holds both the entity and ID values.
     */
    class ValueHolder {
        private final Object entityValue;
        private final Object idValue;

        public ValueHolder(Object entityValue, Object idValue) {
            this.entityValue = entityValue;
            this.idValue = idValue;
        }

        public Object getEntityValue() {
            return entityValue;
        }

        public Object getIdValue() {
            return idValue;
        }
    }
}
