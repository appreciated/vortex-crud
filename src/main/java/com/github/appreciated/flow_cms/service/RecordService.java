package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.Record;
import com.github.appreciated.flow_cms.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    public Optional<Record> findById(Long id) {
        return recordRepository.findById(id);
    }

    public Record save(Record record) {
        return recordRepository.save(record);
    }

    public void deleteById(Long id) {
        recordRepository.deleteById(id);
    }
}
