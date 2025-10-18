package com.github.appreciated.vortex_crud.ui_test_base.pages;

import com.vaadin.testbench.elements.VerticalLayoutElement;

public class MainViewPage extends VerticalLayoutElement {

    public boolean isMainViewVisible() {
        return !$(VerticalLayoutElement.class).id("main-view").hasAttribute("hidden");
    }
}