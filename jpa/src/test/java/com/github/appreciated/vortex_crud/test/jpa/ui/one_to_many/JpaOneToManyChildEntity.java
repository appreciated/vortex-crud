package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.*;

@Entity
@Table(name = "one_to_many_child")
public class JpaOneToManyChildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(TextFieldFactory.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private JpaOneToManyParentEntity parent;

    public JpaOneToManyChildEntity() {
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

    public JpaOneToManyParentEntity getParent() {
        return parent;
    }

    public void setParent(JpaOneToManyParentEntity parent) {
        this.parent = parent;
    }
}
