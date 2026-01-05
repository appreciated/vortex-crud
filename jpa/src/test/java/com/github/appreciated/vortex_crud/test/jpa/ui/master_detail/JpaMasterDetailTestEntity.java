package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;


import com.github.appreciated.vortex_crud.jpa.service.annoations.NumericIdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "master_detail_tasks")
public class JpaMasterDetailTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Integer id;

    @TextField
    private String title;

    @TextField
    private String description;

    @TextField
    private String status;
}
