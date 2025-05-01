package com.github.appreciated.vortex_crud.example.jpa.repository;

import com.github.appreciated.vortex_crud.example.jpa.entity.TaskHasTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHasTaskRepository extends JpaRepository<TaskHasTask, Long> {
    List<TaskHasTask> findByTaskId(Long taskId);
    List<TaskHasTask> findByRelatedTaskId(Long relatedTaskId);
}