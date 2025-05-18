package com.github.appreciated.vortex_crud.example.jpa.entity;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateTimePickerFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldRenderer;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JpaFieldRenderer(TextFieldFactory.class)
    @Column(name = "title", nullable = false, length = 255)
    @Nonnull
    @Length(max = 255)
    private String title;

    @JpaFieldRenderer(TextFieldFactory.class)
    @Length(max = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedTo;

    @JpaFieldRenderer(TextFieldFactory.class)
    @Length(max = 50)
    private String status;

    @JpaFieldRenderer(DateFieldFactory.class)
    private LocalDate dueDate;

    @JpaFieldRenderer(DateTimePickerFactory.class)
    private LocalDateTime createdAt;

    @JpaFieldRenderer(DateTimePickerFactory.class)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskComment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "task_has_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "related_task_id")
    )
    private Set<Task> relatedTasks = new HashSet<>();

    @ManyToMany(mappedBy = "relatedTasks")
    private Set<Task> relatedToTasks = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TaskComment> getComments() {
        return comments;
    }

    public void setComments(List<TaskComment> comments) {
        this.comments = comments;
    }

    public Set<Task> getRelatedTasks() {
        return relatedTasks;
    }

    public void setRelatedTasks(Set<Task> relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    public Set<Task> getRelatedToTasks() {
        return relatedToTasks;
    }

    public void setRelatedToTasks(Set<Task> relatedToTasks) {
        this.relatedToTasks = relatedToTasks;
    }
}