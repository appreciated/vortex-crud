package com.github.appreciated.vortex_crud.core.config.model;

import java.util.List;

public interface AccessControlled {
    void setWriteRoles(List<String> writeRoles);
    List<String> getWriteRoles();
    void setReadOnlyRoles(List<String> readOnlyRoles);
    List<String> getReadOnlyRoles();
}
