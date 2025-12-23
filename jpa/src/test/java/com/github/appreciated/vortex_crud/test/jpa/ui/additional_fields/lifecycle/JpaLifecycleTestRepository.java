package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.lifecycle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLifecycleTestRepository extends JpaRepository<JpaLifecycleTestEntity, Long> {
}