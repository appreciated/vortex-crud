package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSecurityRoleRepository extends JpaRepository<JpaSecurityRole, Integer> {
    JpaSecurityRole findByName(String name);
}
