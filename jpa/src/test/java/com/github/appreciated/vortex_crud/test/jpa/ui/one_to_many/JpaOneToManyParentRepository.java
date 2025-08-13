package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOneToManyParentRepository extends JpaRepository<JpaOneToManyParentEntity, Long> {
}
