package com.github.appreciated.vortex_crud.core.auth;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;

import java.util.Optional;

public interface UserManagementService {

    Optional<VortexCrudUser> findByUsername(String username);

    void register(VortexCrudUser user);

    boolean isUsernameTaken(String username);
}
