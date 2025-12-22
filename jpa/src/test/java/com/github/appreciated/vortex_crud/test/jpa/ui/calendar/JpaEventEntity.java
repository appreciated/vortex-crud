package com.github.appreciated.vortex_crud.test.jpa.ui.calendar;

import com.github.appreciated.vortex_crud.jpa.service.annoations.DateTimePickerField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.NumericIdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_events")
public class JpaEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Long id;

    @TextField
    private String title;

    @DateTimePickerField
    private LocalDateTime startDate;

    @DateTimePickerField
    private LocalDateTime endDate;

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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
