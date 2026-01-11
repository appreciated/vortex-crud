package com.github.appreciated.vortex_crud.core.config.model.validation;

import com.vaadin.flow.data.binder.Validator;

public interface RegexValidatorFactory<T> {
    Validator<T> create(String regex, String errorMessage);
}
