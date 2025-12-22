package com.github.appreciated.vortex_crud.core.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
@AllArgsConstructor
public class DataStoreHooks<ModelClass> {
    @Singular
    private List<DataStoreHook<ModelClass>> beforeCreates;
    @Singular
    private List<DataStoreHook<ModelClass>> afterCreates;
    @Singular
    private List<DataStoreHook<ModelClass>> beforeUpdates;
    @Singular
    private List<DataStoreHook<ModelClass>> afterUpdates;
    @Singular
    private List<DataStoreHook<ModelClass>> beforeDeletes;
    @Singular
    private List<DataStoreHook<ModelClass>> afterDeletes;
    @Singular
    private List<DataStoreHook<ModelClass>> afterReads;

    public DataStoreHooks() {
        this.beforeCreates = new ArrayList<>();
        this.afterCreates = new ArrayList<>();
        this.beforeUpdates = new ArrayList<>();
        this.afterUpdates = new ArrayList<>();
        this.beforeDeletes = new ArrayList<>();
        this.afterDeletes = new ArrayList<>();
        this.afterReads = new ArrayList<>();
    }
}
