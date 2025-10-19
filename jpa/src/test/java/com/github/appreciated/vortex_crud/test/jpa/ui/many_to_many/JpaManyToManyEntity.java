package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "many_to_many_item")
public class JpaManyToManyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @TextField
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "many_to_many_item_relation",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "related_item_id"))
    private Set<JpaManyToManyEntity> relatedItems = new HashSet<>();

    public JpaManyToManyEntity() {
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

    public Set<JpaManyToManyEntity> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(Set<JpaManyToManyEntity> relatedItems) {
        this.relatedItems = relatedItems;
    }
}
