package com.github.appreciated.vortex_crud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateTimeRange implements Serializable {
    private LocalDateTime start;
    private LocalDateTime end;
}
