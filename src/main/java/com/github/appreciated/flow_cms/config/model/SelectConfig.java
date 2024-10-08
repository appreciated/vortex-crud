package com.github.appreciated.flow_cms.config.model;

import java.util.Map;

public class SelectConfig {

    private Map<String, Translatable> taskStatus;

    public Map<String, Translatable> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Map<String, Translatable> taskStatus) {
        this.taskStatus = taskStatus;
    }
}
