package com.github.appreciated.vortex_crud.example.jpa.entity;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import com.github.appreciated.vortex_crud.jpa.service.datastore.ReferenceFieldConfiguration;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Field(TextFieldFactory.class)
    @Column(name = "title", nullable = false)
    @Nonnull
    private String title;

    @Field(TextAreaFieldFactory.class)
    @Length(max = 1000)
    private String description;

    @Field(ReferenceFieldFactory.class)
    @ReferenceFieldConfiguration(value = "username")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Field(SelectFieldFactory.class)
    @SelectValues("task-status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Field(DateFieldFactory.class)
    private LocalDate dueDate;

    @Field(DateTimePickerFactory.class)
    private LocalDateTime createdAt;

    @Field(DateTimePickerFactory.class)
    private LocalDateTime updatedAt;

    @Field(ReferenceFieldFactory.class)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TaskComment> comments = new ArrayList<>();

    @Field(ReferenceFieldFactory.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_has_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "related_task_id")
    )
    private List<Task> relatedTasks = new ArrayList<>();

    private Integer rowIndex;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public List<Task> getRelatedTasks() {
        return relatedTasks;
    }

    public void setRelatedTasks(List<Task> relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(assignedTo, task.assignedTo) && status == task.status && Objects.equals(dueDate, task.dueDate) && Objects.equals(createdAt, task.createdAt) && Objects.equals(updatedAt, task.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, assignedTo, status, dueDate, createdAt, updatedAt);
    }
}