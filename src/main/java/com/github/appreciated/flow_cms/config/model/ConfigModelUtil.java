package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigModelUtil {
    public static <T> Map<String, T> toStringMapWithValueType(ConfigObject config, Class<T> clazz) {
        Set<Map.Entry<String, ConfigValue>> entries = config
                .entrySet();
        return entries
                .stream()
                .map(stringConfigValueEntry -> {
                    try {
                        return Map.entry(stringConfigValueEntry.getKey(), ConfigBeanFactory.create(((ConfigObject) stringConfigValueEntry.getValue()).toConfig(), clazz));
                    } catch (Exception e) {
                        LoggerFactory.getLogger(clazz.getName()).error(e.getMessage(), e);
                        LoggerFactory.getLogger(clazz.getName()).error(e.getMessage(), stringConfigValueEntry.getValue().render());
                        return null;
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
