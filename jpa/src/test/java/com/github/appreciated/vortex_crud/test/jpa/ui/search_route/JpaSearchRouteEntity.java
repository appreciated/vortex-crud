package com.github.appreciated.vortex_crud.test.jpa.ui.search_route;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_route_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaSearchRouteEntity {
    @Id
    private Integer id;
    private String name;
}
