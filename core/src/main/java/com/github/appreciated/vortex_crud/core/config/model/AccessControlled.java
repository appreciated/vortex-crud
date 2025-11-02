package com.github.appreciated.vortex_crud.core.config.model;

import java.util.List;

public interface AccessControlled {

    List<String> getWriteRoles();

    List<String> getReadOnlyRoles();
}
