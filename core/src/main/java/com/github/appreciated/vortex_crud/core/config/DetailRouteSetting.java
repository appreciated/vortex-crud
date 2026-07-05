package com.github.appreciated.vortex_crud.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DetailRouteSetting {
    private boolean isWrapped;
    private boolean isHeaderHidden;
    private boolean isCreationMode;
}
