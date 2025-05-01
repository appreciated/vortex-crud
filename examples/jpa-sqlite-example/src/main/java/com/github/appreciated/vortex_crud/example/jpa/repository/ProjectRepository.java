package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}