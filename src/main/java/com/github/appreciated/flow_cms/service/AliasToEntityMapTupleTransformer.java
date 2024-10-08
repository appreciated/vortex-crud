package com.github.appreciated.flow_cms.service;

import org.hibernate.query.TupleTransformer;

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
            result.put(aliases[i].toLowerCase(), tuple[i]);
        }
        return new GenericEntity(result);
    }
}