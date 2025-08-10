package com.github.appreciated.vortex_crud.jpa.service.ui.field_validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldValidationRepository extends JpaRepository<FieldValidationEntity, Long> {
}
