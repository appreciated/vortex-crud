package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldValueStrategy;
import org.springframework.stereotype.Component;

/**
 * jOOQ implementation of ReferenceFieldValueStrategy.
 * jOOQ uses ID values for foreign key fields, not entity objects.
 */
@Component
public class JooqReferenceFieldValueStrategy implements ReferenceFieldValueStrategy {

    @Override
    public Object prepareValueForEntity(Object entityValue, Object idValue, VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        // jOOQ expects ID values, not entity objects
        return idValue;
    }

    @Override
    public ValueHolder processIncomingValue(Object value, VortexCrudDataStore<?, ?> dataStore, VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        if (value == null) {
            return new ValueHolder(null, null);
        }

        // In jOOQ, incoming value is an ID (Number)
        // Load the entity using the ID
        Object entity = dataStore.getRecordById(value);
        return new ValueHolder(entity, value);
    }
}
