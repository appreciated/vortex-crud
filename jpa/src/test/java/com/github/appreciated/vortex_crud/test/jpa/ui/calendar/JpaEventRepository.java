package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventRepository extends JpaRepository<JpaEventEntity, Long> {
}
