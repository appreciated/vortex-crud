package com.github.appreciated.vortex_crud.test.jpa.ui.search_route;

import com.github.appreciated.vortex_crud.jpa.service.annoations.IntegerNumberField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
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
    @IntegerNumberField
    private Integer id;
    @TextField
    private String name;
}
