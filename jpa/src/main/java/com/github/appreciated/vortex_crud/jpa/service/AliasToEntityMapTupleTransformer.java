package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.hibernate.query.TupleTransformer;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A TupleTransformer implementation that converts query results into a GenericEntity object.
 * Transforms each tuple into a map where column aliases are used as keys.
 */

public class AliasToEntityMapTupleTransformer implements TupleTransformer<GenericEntity> {

    @Override
    public GenericEntity transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < aliases.length; i++) {
            Object value = tuple[i];
            if (value instanceof java.sql.Date) {
                value = ((Date) value).toLocalDate();
            }
            result.put(aliases[i].toLowerCase(), value);
        }
        return new GenericEntity(result);
    }
}