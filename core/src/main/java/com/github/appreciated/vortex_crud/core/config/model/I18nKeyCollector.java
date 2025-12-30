package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import org.springframework.util.ReflectionUtils;

import java.util.*;

public interface I18nKeyCollector {

    default List<ResolvedI18nKey> collectI18nKeys() {
        return collectI18nKeys(Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    default List<ResolvedI18nKey> collectI18nKeys(Set<Object> visited) {
        if (visited.contains(this)) {
            return Collections.emptyList();
        }
        visited.add(this);

        List<ResolvedI18nKey> keys = new ArrayList<>();

        ReflectionUtils.doWithFields(this.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(this);

            if (value == null) {
                return;
            }

            if (field.isAnnotationPresent(I18nKey.class)) {
                collectKeysFromValue(value, field.getName(), this.getClass().getSimpleName(), keys);
            } else {
                // Recurse into children
                collectKeysFromChild(value, visited, keys);
            }
        });

        return keys;
    }

    private void collectKeysFromValue(Object value, String fieldName, String className, List<ResolvedI18nKey> keys) {
        if (value instanceof String) {
            keys.add(new ResolvedI18nKey((String) value, className + "." + fieldName));
        } else if (value instanceof java.util.Collection<?>) {
            ((java.util.Collection<?>) value).forEach(item -> {
                if (item instanceof String) {
                    keys.add(new ResolvedI18nKey((String) item, className + "." + fieldName));
                }
            });
        } else if (value instanceof Map<?, ?>) {
            ((Map<?, ?>) value).values().forEach(item -> {
                if (item instanceof String) {
                    keys.add(new ResolvedI18nKey((String) item, className + "." + fieldName));
                }
            });
        }
    }

    private void collectKeysFromChild(Object value, Set<Object> visited, List<ResolvedI18nKey> keys) {
        if (value instanceof I18nKeyCollector) {
            keys.addAll(((I18nKeyCollector) value).collectI18nKeys(visited));
        } else if (value instanceof java.util.Collection<?>) {
            for (Object item : (java.util.Collection<?>) value) {
                if (item instanceof I18nKeyCollector) {
                    keys.addAll(((I18nKeyCollector) item).collectI18nKeys(visited));
                }
            }
        } else if (value instanceof Map<?, ?>) {
            for (Object item : ((Map<?, ?>) value).values()) {
                if (item instanceof I18nKeyCollector) {
                    keys.addAll(((I18nKeyCollector) item).collectI18nKeys(visited));
                }
            }
        }
    }
}
