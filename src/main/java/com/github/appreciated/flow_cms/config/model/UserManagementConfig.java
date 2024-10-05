package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class UserManagementConfig {
    private boolean enabled;
    @Optional
    private AccessControlConfig access_control;
    private boolean registration;
    private List<AdditionalTableField> additional_table_fields;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AccessControlConfig getAccess_control() {
        return access_control;
    }

    public void setAccess_control(AccessControlConfig access_control) {
        this.access_control = access_control;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public List<AdditionalTableField> getAdditional_table_fields() {
        return additional_table_fields;
    }

    public void setAdditional_table_fields(List<AdditionalTableField> additional_table_fields) {
        this.additional_table_fields = additional_table_fields;
    }
}

