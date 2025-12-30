package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Selects implements I18nKeyProvider {
    private Map<String, LinkedHashMap<?, String>> configs;

    @Override
    public Collection<String> getI18nKeys() {
        List<String> keys = new ArrayList<>();
        if (configs != null) {
            configs.values().forEach(map -> {
                if (map != null) {
                    keys.addAll(map.values());
                }
            });
        }
        return keys;
    }
}
