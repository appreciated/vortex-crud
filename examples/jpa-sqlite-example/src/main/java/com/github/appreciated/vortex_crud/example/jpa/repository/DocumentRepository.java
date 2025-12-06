package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
