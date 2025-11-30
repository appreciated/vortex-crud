package com.github.appreciated.vortex_crud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateRange implements Serializable {
    private LocalDate start;
    private LocalDate end;
}
