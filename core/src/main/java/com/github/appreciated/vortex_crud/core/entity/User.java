package com.github.appreciated.vortex_crud.core.entity;

import java.util.List;

public interface User {
    String getUsername();

    String getPassword();

    List<String> getRoles();
}