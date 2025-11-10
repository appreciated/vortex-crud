package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMultiFormRepository extends JpaRepository<JpaMultiFormEntity, Long> {
}
