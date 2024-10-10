package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class UserManagementConfig {

    private boolean enabled;
    @Optional
    private AccessControlConfig accessControl;
    private boolean registration;
    private List<AdditionalColumn> additionalColumns;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AccessControlConfig getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControlConfig accessControl) {
        this.accessControl = accessControl;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public List<AdditionalColumn> getAdditionalColumns() {
        return additionalColumns;
    }

    public void setAdditionalColumns(List<AdditionalColumn> additionalColumns) {
        this.additionalColumns = additionalColumns;
    }
}

