package com.rafaeltmbr.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    TaskModel findByTitle(String title);
}
