package com.github.appreciated.flow_cms.service;

import org.hibernate.query.TupleTransformer;

import java.util.HashMap;
import java.util.Map;

public class AliasToEntityMapTupleTransformer implements TupleTransformer<Map<String, Object>> {

    @Override
    public Map<String, Object> transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < aliases.length; i++) {
            result.put(aliases[i].toLowerCase(), tuple[i]);
        }
        return result;
    }
}