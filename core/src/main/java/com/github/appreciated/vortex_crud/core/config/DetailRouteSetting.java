package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;

public class DetailRouteSetting {
    private final boolean isWrapped;
    private final boolean isHeaderHidden;
    private final boolean isCreationMode;
    private final DataStoreConfig dataStoreConfig;

    public DetailRouteSetting(boolean isWrapped, boolean isHeaderHidden, boolean isCreationMode) {
        this(isWrapped, isHeaderHidden, isCreationMode, null);
    }

    public DetailRouteSetting(boolean isWrapped, boolean isHeaderHidden, boolean isCreationMode, DataStoreConfig dataStoreConfig) {
        this.isWrapped = isWrapped;
        this.isHeaderHidden = isHeaderHidden;
        this.isCreationMode = isCreationMode;
        this.dataStoreConfig = dataStoreConfig;
    }

    public boolean isWrapped() {
        return isWrapped;
    }

    public boolean isHeaderHidden() {
        return isHeaderHidden;
    }

    public boolean isCreationMode() {
        return isCreationMode;
    }

    public DataStoreConfig dataStoreConfig() {
        return dataStoreConfig;
    }
}
