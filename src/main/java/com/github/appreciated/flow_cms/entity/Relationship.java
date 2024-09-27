package com.github.appreciated.flow_cms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_collection_id", nullable = false)
    private Collection sourceCollection;

    @ManyToOne
    @JoinColumn(name = "target_collection_id", nullable = false)
    private Collection targetCollection;

    @Column(nullable = false)
    private String relationType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(Collection sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public Collection getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(Collection targetCollection) {
        this.targetCollection = targetCollection;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
