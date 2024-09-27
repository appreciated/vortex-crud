package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.entity.Relationship;
import com.github.appreciated.flow_cms.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;

    public List<Relationship> findAll() {
        return relationshipRepository.findAll();
    }

    public Optional<Relationship> findById(Long id) {
        return relationshipRepository.findById(id);
    }

    public Relationship save(Relationship relationship) {
        return relationshipRepository.save(relationship);
    }

    public void deleteById(Long id) {
        relationshipRepository.deleteById(id);
    }
}
