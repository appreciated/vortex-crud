package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "validation_test_email")
public class JpaEmailFieldValidationEntity extends JpaFieldValidationEntityBase {
}
