package com.github.appreciated.vortex_crud.core.config.model;

import lombok.Builder;
import lombok.With;

import java.util.LinkedHashMap;
import java.util.Map;

@Builder(toBuilder = true)
@With
public record Selects(
    Map<String, LinkedHashMap<?, String>> configs
) {
    // Explicit getters for backwards compatibility
    public Map<String, LinkedHashMap<?, String>> getConfigs() {
        return configs;
    }
}