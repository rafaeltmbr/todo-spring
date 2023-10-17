package com.rafaeltmbr.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByTitle(String title);

    List<TaskModel> findByUserId(UUID idUser);
}
