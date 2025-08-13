package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "one_to_many_parent")
public class JpaOneToManyParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(TextFieldFactory.class)
    private String name;

    // Bidirectional for convenience (not required by tests)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaOneToManyChildEntity> children = new ArrayList<>();

    public JpaOneToManyParentEntity() {
    }

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

    public List<JpaOneToManyChildEntity> getChildren() {
        return children;
    }

    public void setChildren(List<JpaOneToManyChildEntity> children) {
        this.children = children;
    }
}
