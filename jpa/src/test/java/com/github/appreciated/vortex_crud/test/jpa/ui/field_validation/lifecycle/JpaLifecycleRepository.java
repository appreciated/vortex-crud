package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle;

import com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.lifecycle.JpaLifecycleEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLifecycleRepository extends JpaRepository<JpaLifecycleEntity, Long> {
}
