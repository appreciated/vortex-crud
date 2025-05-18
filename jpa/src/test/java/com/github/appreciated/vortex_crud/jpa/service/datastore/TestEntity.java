package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.NumberFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldRenderer;
import jakarta.persistence.*;

/**
 * Entity class representing the "test_table".
 */
@Entity
@Table(name = "test_table")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JpaFieldRenderer(TextFieldFactory.class)
    @Column(name = "name", nullable = false)
    private String name;

    @JpaFieldRenderer(NumberFieldFactory.class)
    @Column(name = "age", nullable = false)
    private Integer age;

    // Default constructor
    public TestEntity() {}

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
