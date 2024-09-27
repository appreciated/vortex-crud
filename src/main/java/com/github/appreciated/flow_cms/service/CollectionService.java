package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.Collection;
import com.github.appreciated.flow_cms.repository.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {
    @Autowired
    private CollectionRepository collectionRepository;

    public List<Collection> findAll() {
        return collectionRepository.findAll();
    }

    public Optional<Collection> findById(Long id) {
        return collectionRepository.findById(id);
    }

    public Collection save(Collection collection) {
        return collectionRepository.save(collection);
    }

    public void deleteById(Long id) {
        collectionRepository.deleteById(id);
    }
}
