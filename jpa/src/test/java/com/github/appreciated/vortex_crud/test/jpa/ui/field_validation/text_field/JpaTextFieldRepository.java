package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.text_field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTextFieldRepository extends JpaRepository<JpaTextFieldEntity, Long> {
}
