package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "text_field_test")
public class JpaTextFieldEntity extends AbstractFieldValidationEntity {
}
