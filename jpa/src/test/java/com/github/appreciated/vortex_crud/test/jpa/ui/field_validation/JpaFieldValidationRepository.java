package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFieldValidationRepository extends JpaRepository<BaseJpaFieldValidationEntity, Long> {
}

@Repository
interface JpaEmailFieldValidationRepository extends JpaRepository<JpaEmailFieldValidationEntity, Long> {
}

@Repository
interface JpaDateFieldValidationRepository extends JpaRepository<JpaDateFieldValidationEntity, Long> {
}

@Repository
interface JpaCheckboxFieldValidationRepository extends JpaRepository<JpaCheckboxFieldValidationEntity, Long> {
}

@Repository
interface JpaDateTimeFieldValidationRepository extends JpaRepository<JpaDateTimeFieldValidationEntity, Long> {
}

@Repository
interface JpaImageFieldValidationRepository extends JpaRepository<JpaImageFieldValidationEntity, Long> {
}

@Repository
interface JpaNumberFieldValidationRepository extends JpaRepository<JpaNumberFieldValidationEntity, Long> {
}

@Repository
interface JpaTextFieldValidationRepository extends JpaRepository<JpaTextFieldValidationEntity, Long> {
}

@Repository
interface JpaSelectFieldValidationRepository extends JpaRepository<JpaSelectFieldValidationEntity, Long> {
}

@Repository
interface JpaLifecycleFieldValidationRepository extends JpaRepository<JpaLifecycleFieldValidationEntity, Long> {
}
