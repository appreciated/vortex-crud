package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "many_to_many_item")
@Getter
@Setter
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

}
