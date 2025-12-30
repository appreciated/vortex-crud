package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.util.*;

public class I18nKeyResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nKeyResolver.class);

    public record ResolvedI18nKey(String key, String context) {}

    public List<ResolvedI18nKey> resolveKeys(Object target) {
        if (target == null) {
            return Collections.emptyList();
        }
        List<ResolvedI18nKey> keys = new ArrayList<>();
        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        traverse(target, visited, keys);
        return keys;
    }

    private void traverse(Object target, Set<Object> visited, List<ResolvedI18nKey> keys) {
        if (target == null || visited.contains(target)) {
            return;
        }
        visited.add(target);

        ReflectionUtils.doWithFields(target.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(target);

            if (value == null) {
                return;
            }

            if (field.isAnnotationPresent(I18nKey.class)) {
                collectKeys(value, field.getName(), target.getClass().getSimpleName(), keys);
            } else if (value instanceof Collection<?>) {
                for (Object item : (Collection<?>) value) {
                    traverse(item, visited, keys);
                }
            } else if (value instanceof Map<?, ?>) {
                for (Object item : ((Map<?, ?>) value).values()) {
                    traverse(item, visited, keys);
                }
            } else if (isComplexObject(value)) {
                traverse(value, visited, keys);
            }
        });
    }

    private void collectKeys(Object value, String fieldName, String className, List<ResolvedI18nKey> keys) {
        if (value instanceof String) {
            keys.add(new ResolvedI18nKey((String) value, className + "." + fieldName));
        } else if (value instanceof Collection<?>) {
            ((Collection<?>) value).forEach(item -> {
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

    private boolean isComplexObject(Object value) {
        String packageName = value.getClass().getPackage().getName();
        return packageName.startsWith("com.github.appreciated");
    }
}
