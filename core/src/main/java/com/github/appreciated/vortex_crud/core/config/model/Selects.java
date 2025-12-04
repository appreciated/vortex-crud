package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Selects {
    private Map<String, LinkedHashMap<?, String>> configs;

    public Map<String, LinkedHashMap<?, String>> configs() {
        return configs;
    }
}
