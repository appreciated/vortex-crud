package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "many_to_many_item")
public class ManyToManyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(TextFieldFactory.class)
    private String name;

    @ManyToMany
    @JoinTable(name = "many_to_many_item_relation",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "related_item_id"))
    private Set<ManyToManyEntity> relatedItems = new HashSet<>();

    public ManyToManyEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<ManyToManyEntity> getRelatedItems() { return relatedItems; }
    public void setRelatedItems(Set<ManyToManyEntity> relatedItems) { this.relatedItems = relatedItems; }
}
