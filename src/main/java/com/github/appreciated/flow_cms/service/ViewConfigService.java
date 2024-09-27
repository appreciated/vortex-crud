package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.ViewConfig;
import com.github.appreciated.flow_cms.repository.ViewConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViewConfigService {
    @Autowired
    private ViewConfigRepository viewConfigRepository;

    public List<ViewConfig> findAll() {
        return viewConfigRepository.findAll();
    }

    public Optional<ViewConfig> findById(Long id) {
        return viewConfigRepository.findById(id);
    }

    public ViewConfig save(ViewConfig viewConfig) {
        return viewConfigRepository.save(viewConfig);
    }

    public void deleteById(Long id) {
        viewConfigRepository.deleteById(id);
    }
}
