package com.github.appreciated.vortex_crud.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DetailRouteSetting {
    private final boolean isWrapped;
    private final boolean isHeaderHidden;
    private final boolean isCreationMode;
}
