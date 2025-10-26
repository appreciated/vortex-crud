package com.github.appreciated.vortex_crud.core.entity;

import java.util.Set;

public interface VortexCrudUser {
    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    Set<String> getRoles();

    void setRoles(Set<String> roles);
}
