package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for all hooks associated with a specific entity type.
 * Provides methods to register and retrieve hooks for different operations.
 *
 * @param <ModelClass> The entity type
 */
@Accessors(fluent = true)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataStoreHooks<ModelClass> {
    @Singular
    private List<DataStoreHook<ModelClass>> beforeCreates = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> afterCreates = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> beforeUpdates = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> afterUpdates = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> beforeDeletes = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> afterDeletes = new ArrayList<>();
    @Singular
    private List<DataStoreHook<ModelClass>> afterReads = new ArrayList<>();
}
