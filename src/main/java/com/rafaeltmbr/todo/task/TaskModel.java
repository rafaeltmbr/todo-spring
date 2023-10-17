package com.rafaeltmbr.todo.task;

import com.rafaeltmbr.todo.error.AppError;
import com.rafaeltmbr.todo.shared.Config;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity(name = "task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID userId;

    @Column(length = Config.taskTitleMaximumLength)
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws AppError {
        if (title == null) throw new AppError("Title is required");

        title = title.trim();
        if (title.isEmpty()) throw new AppError("Title must not be empty.");

        if (title.length() > Config.taskTitleMaximumLength) {
            throw new AppError("Title must be at most 50 characters long.");
        }

        this.title = title;
    }
}
