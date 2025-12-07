package com.github.appreciated.vortex_crud.example.jooq.converter;

import org.jooq.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TagsConverter implements Converter<String, Set> {

    @Override
    public Set from(String databaseObject) {
         if (databaseObject == null || databaseObject.isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(databaseObject.split(","))
                .collect(Collectors.toSet());
    }

    @Override
    public String to(Set userObject) {
        if (userObject == null || userObject.isEmpty()) {
            return null;
        }
        return String.join(",", (Set<String>)userObject);
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<Set> toType() {
        return Set.class;
    }
}
