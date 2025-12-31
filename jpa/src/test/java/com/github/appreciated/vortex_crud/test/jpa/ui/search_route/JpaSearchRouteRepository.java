package com.github.appreciated.vortex_crud.test.jpa.ui.search_route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSearchRouteRepository extends JpaRepository<JpaSearchRouteEntity, Integer> {
}
