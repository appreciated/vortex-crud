package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSelectFieldRepository extends JpaRepository<JpaSelectFieldEntity, Long> {
}
