package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectRepository extends JpaRepository<JpaProjectEntity, Integer> {
}
