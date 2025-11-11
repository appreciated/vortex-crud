package com.github.appreciated.vortex_crud.test.jpa.ui.multi_select_field;

import com.github.appreciated.vortex_crud.jpa.service.annoations.MultiSelectField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class JpaMultiSelectFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    @TextField
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @MultiSelectField(value = "name", fields = {"name"})
    private Set<JpaMultiSelectFieldCategoryEntity> categories = new HashSet<>();

    // Default constructor
    public JpaMultiSelectFieldEntity() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<JpaMultiSelectFieldCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<JpaMultiSelectFieldCategoryEntity> categories) {
        this.categories = categories;
    }
}
