package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSecurityUserRepository extends JpaRepository<JpaSecurityUser, Integer> {
    JpaSecurityUser findByUsername(String username);
}
