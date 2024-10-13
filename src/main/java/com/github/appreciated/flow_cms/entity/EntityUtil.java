package com.github.appreciated.flow_cms.entity;

import com.github.appreciated.flow_cms.service.GenericEntity;

public class EntityUtil {
    public static String getId(GenericEntity record) {
        return "" + record.get("id"); //TODO Cleanup, Column name and type needs to be declared in config
    }

    public static boolean isNew(GenericEntity entity) {
        return entity.get("id") == null; //TODO Cleanup, Column name needs to be declared in config
    }
}
