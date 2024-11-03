package com.github.appreciated.turbo_crud.entity;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DefaultTypeMappingConfiguration implements TurboCrudTypeMappingConfiguration {
    @Override
    public HashMap<Object, Object> getTypeMappings() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("number", List.of("INTEGER", "BIGINT", "SMALLINT", "DECIMAL", "NUMERIC"));
        map.put("id", List.of("UUID", "INTEGER", "CHAR", "VARCHAR"));
        map.put("text", List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "CLOB"));
        map.put("textarea", List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "CLOB"));
        map.put("date", List.of("DATE"));
        map.put("datetime", List.of("TIMESTAMP", "TIMESTAMP WITH TIME ZONE", "DATETIME"));
        map.put("boolean", List.of("BOOLEAN", "BIT"));
        map.put("select", List.of("VARCHAR", "CHARACTER VARYING"));
        map.put("reference", map.get("id"));
        map.put("image", List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "BLOB"));
        return map;
    }
}
