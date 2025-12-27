package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation.checkbox_field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCheckboxFieldRepository extends JpaRepository<JpaCheckboxFieldEntity, Long> {
}
