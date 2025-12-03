package com.github.appreciated.vortex_crud.core.config;

public class DetailRouteSetting {
    private final boolean isWrapped;
    private final boolean isHeaderHidden;
    private final boolean isCreationMode;

    public DetailRouteSetting(boolean isWrapped, boolean isHeaderHidden, boolean isCreationMode) {
        this.isWrapped = isWrapped;
        this.isHeaderHidden = isHeaderHidden;
        this.isCreationMode = isCreationMode;
    }

    public boolean isWrapped() { return isWrapped; }
    public boolean isHeaderHidden() { return isHeaderHidden; }
    public boolean isCreationMode() { return isCreationMode; }
}
