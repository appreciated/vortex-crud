package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldValueStrategy;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of ReferenceFieldValueStrategy.
 * JPA uses entity objects for foreign key fields, not IDs.
 */
@Component
public class JpaReferenceFieldValueStrategy implements ReferenceFieldValueStrategy {

    @Override
    public Object prepareValueForEntity(Object entityValue, Object idValue, VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        // JPA expects entity objects, not IDs
        return entityValue;
    }

    @Override
    public ValueHolder processIncomingValue(Object value, VortexCrudDataStore<?, ?> dataStore, VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        if (value == null) {
            return new ValueHolder(null, null);
        }

        // In JPA, incoming value is an entity object
        // Extract the ID and reload the entity to ensure consistency with data provider
        String idString = dataStoreUtil.getId(value);
        if (idString != null) {
            Object idValue = parseId(idString);
            // Reload entity from datastore to ensure it's fresh and compatible with data provider
            Object reloadedEntity = dataStore.getRecordById(idValue);
            return new ValueHolder(reloadedEntity, idValue);
        }

        return new ValueHolder(null, null);
    }

    private Object parseId(String idString) {
        try {
            return Integer.valueOf(idString);
        } catch (NumberFormatException e) {
            try {
                return Long.valueOf(idString);
            } catch (NumberFormatException ex) {
                return idString;
            }
        }
    }
}
