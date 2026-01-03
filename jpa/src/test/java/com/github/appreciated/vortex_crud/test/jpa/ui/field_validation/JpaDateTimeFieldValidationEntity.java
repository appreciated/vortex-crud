package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "datetime_validation_test")
@Getter
@Setter
@NoArgsConstructor
public class JpaDateTimeFieldValidationEntity extends BaseJpaFieldValidationEntity {
}
