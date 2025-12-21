package com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dropdown_items")
public class JpaDropdownEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
