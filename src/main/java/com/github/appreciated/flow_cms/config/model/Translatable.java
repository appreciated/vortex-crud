package com.github.appreciated.flow_cms.config.model;

import com.vaadin.flow.component.UI;

import java.util.HashMap;
import java.util.Map;

public class Translatable extends HashMap<String, String> {

    public Translatable(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public String getTranslation(UI ui) {
        String country = ui.getLocale().getCountry();
        return getOrDefault(country.toLowerCase(), getOrDefault("en", "missing translation"));
    }
}
