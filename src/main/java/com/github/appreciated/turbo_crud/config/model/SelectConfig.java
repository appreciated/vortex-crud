package com.github.appreciated.turbo_crud.config.model;

import java.util.Map;

public class SelectConfig {

    private Map<String, String> taskStatus;

    public Map<String, String> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Map<String, String> taskStatus) {
        this.taskStatus = taskStatus;
    }
}
