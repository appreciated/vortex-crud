package com.github.appreciated.vortex_crud.test.jpa.ui.single_component_route;

import com.github.appreciated.vortex_crud.jpa.service.annoations.MarkDownField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "single_form_route_test")
public class JpaSingleComponentRouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @TextField
    @Column(name = "name")
    private String name;

    @Column(name = "notes", columnDefinition = "TEXT")
    @MarkDownField
    private String notes;
}
