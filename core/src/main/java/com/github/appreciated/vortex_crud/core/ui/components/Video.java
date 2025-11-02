package com.github.appreciated.vortex_crud.core.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.server.streams.AbstractDownloadHandler;
import com.vaadin.flow.server.streams.DownloadHandler;

@Tag("video")
public class Video extends HtmlContainer implements ClickNotifier<Video>, HasAriaLabel {

    private static final String SRC_ATTRIBUTE = "src";
    private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors
            .attributeWithDefault(SRC_ATTRIBUTE, "");

    public Video() {
        super();
    }

    public Video(String src) {
        setSrc(src);
    }

    public Video(DownloadHandler downloadHandler) {
        setSrc(downloadHandler);
    }

    public String getSrc() {
        return get(srcDescriptor);
    }

    public void setSrc(String src) {
        set(srcDescriptor, src);
    }

    public void setSrc(DownloadHandler downloadHandler) {
        if (downloadHandler instanceof AbstractDownloadHandler<?> handler) {
            handler.inline();
        }
        getElement().setAttribute(SRC_ATTRIBUTE, downloadHandler);
    }

    public void setControls(boolean enabled) {
        if (enabled) {
            getElement().setAttribute("controls", true);
        } else {
            getElement().removeAttribute("controls");
        }
    }

    public void setAutoplay(boolean enabled) {
        if (enabled) {
            getElement().setAttribute("autoplay", true);
        } else {
            getElement().removeAttribute("autoplay");
        }
    }

    public void setLoop(boolean enabled) {
        if (enabled) {
            getElement().setAttribute("loop", true);
        } else {
            getElement().removeAttribute("loop");
        }
    }

    public void setMuted(boolean enabled) {
        if (enabled) {
            getElement().setAttribute("muted", true);
        } else {
            getElement().removeAttribute("muted");
        }
    }

    public void setPreload(String preload) {
        if (preload != null && !preload.isBlank()) {
            getElement().setAttribute("preload", preload);
        } else {
            getElement().removeAttribute("preload");
        }
    }
}
