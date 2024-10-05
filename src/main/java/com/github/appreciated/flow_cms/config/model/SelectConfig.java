package com.github.appreciated.flow_cms.config.model;

import java.util.Map;

public class SelectConfig {
    private Map<String, Map<String, String>> task_status;

    public Map<String, Map<String, String>> getTask_status() {
        return task_status;
    }

    public void setTask_status(Map<String, Map<String, String>> task_status) {
        this.task_status = task_status;
    }
}
