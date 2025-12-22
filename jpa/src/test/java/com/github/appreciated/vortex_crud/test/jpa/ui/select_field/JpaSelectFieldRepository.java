package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSelectFieldRepository extends JpaRepository<JpaSelectFieldEntity, Integer> {
}
