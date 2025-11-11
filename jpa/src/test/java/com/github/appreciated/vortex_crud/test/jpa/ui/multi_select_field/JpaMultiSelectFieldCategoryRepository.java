package com.github.appreciated.vortex_crud.test.jpa.ui.multi_select_field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMultiSelectFieldCategoryRepository extends JpaRepository<JpaMultiSelectFieldCategoryEntity, Long> {
}
