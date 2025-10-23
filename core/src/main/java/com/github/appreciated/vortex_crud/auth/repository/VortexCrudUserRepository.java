package com.github.appreciated.vortex_crud.auth.repository;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface VortexCrudUserRepository extends UserDetailsService {
    Optional<VortexCrudUser> findByUsername(String username);

    VortexCrudUser save(VortexCrudUser user);

    boolean existsByUsername(String username);
}
