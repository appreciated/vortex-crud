package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigModelUtil {

    public static <T> Map<String, T> toStringMapWithValueType(ConfigObject config, Class<T> clazz) {
        Set<Map.Entry<String, ConfigValue>> entries = config
                .entrySet();
        try {
            return entries
                    .stream()
                    .map(stringConfigValueEntry -> {
                        T value;
                        if (clazz == String.class) {
                            value = (T) stringConfigValueEntry.getValue().unwrapped();
                        } else {
                            value = ConfigBeanFactory.create(((ConfigObject) stringConfigValueEntry.getValue()).toConfig(), clazz);
                        }
                        return Map.entry(stringConfigValueEntry.getKey(), value);
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

}
