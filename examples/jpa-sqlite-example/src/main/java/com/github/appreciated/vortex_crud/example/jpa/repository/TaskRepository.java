package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByStatus(String status);

    List<Task> findByAssignedToId(Long userId);
}