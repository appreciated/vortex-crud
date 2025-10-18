package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.entity.User;

import java.util.Optional;

public interface UserProvider<T extends User> {
    Optional<T> findByUsername(String username);

    T registerUser(String username, String password);
}