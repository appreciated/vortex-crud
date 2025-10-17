package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;

public class VideoDisplayComponent extends Video {

    private final VortexCrudResourceProvider resourceProvider;

    public VideoDisplayComponent(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void setVideoSource(String src) {
        if (src != null) {
            setSrc(resourceProvider.getResource(src));
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

}