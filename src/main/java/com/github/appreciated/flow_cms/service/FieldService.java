package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.Field;
import com.github.appreciated.flow_cms.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldService {
    @Autowired
    private FieldRepository fieldRepository;

    public List<Field> findAll() {
        return fieldRepository.findAll();
    }

    public Optional<Field> findById(Long id) {
        return fieldRepository.findById(id);
    }

    public Field save(Field field) {
        return fieldRepository.save(field);
    }

    public void deleteById(Long id) {
        fieldRepository.deleteById(id);
    }
}
