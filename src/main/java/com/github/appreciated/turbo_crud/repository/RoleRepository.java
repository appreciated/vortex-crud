package com.github.appreciated.turbo_crud.repository;

import com.github.appreciated.turbo_crud.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
