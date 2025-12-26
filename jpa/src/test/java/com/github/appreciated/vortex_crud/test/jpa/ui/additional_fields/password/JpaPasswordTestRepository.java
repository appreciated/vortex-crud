package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPasswordTestRepository extends JpaRepository<JpaPasswordTestEntity, Long> {
}