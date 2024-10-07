package com.github.appreciated.flow_cms.config.model;

import java.util.Map;

public class SelectConfig {

    private Map<String, Translatable> task_status;

    public Map<String, Translatable> getTask_status() {
        return task_status;
    }

    public void setTask_status(Map<String, Translatable> task_status) {
        this.task_status = task_status;
    }
}
