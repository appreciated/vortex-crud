package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class UserManagementConfig {

    private boolean enabled;
    @Optional
    private AccessControlConfig accessControl;
    private boolean signUp;
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

    public boolean isSignUp() {
        return signUp;
    }

    public void setSignUp(boolean signUp) {
        this.signUp = signUp;
    }

    public List<AdditionalColumn> getAdditionalColumns() {
        return additionalColumns;
    }

    public void setAdditionalColumns(List<AdditionalColumn> additionalColumns) {
        this.additionalColumns = additionalColumns;
    }
}

