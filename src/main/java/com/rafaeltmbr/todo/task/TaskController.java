package com.rafaeltmbr.todo.task;


import com.rafaeltmbr.todo.shared.AppError;
import com.rafaeltmbr.todo.shared.Config;
import com.rafaeltmbr.todo.shared.Merge;
import com.rafaeltmbr.todo.shared.ResponseError;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @GetMapping
    public List<TaskModel> list(ServletRequest request) {
        return taskRepository.findByUserId((UUID) request.getAttribute("userId"));
    }

    /**
     * Validates the given task parameters and create a new task.
     *
     * @param taskModel the new task to be created.
     * @param request   the HttpServletRequest.
     * @return ResponseEntity with the newly created task.
     */

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        try {
            var userId = (UUID) request.getAttribute("userId");
            validateTitle(taskModel.getTitle(), userId, null);
            validateDates(taskModel);

            taskModel.setUserId(userId);
            taskModel.setTitle(taskModel.getTitle().trim());

            return ResponseEntity.ok(taskRepository.save(taskModel));
        } catch (AppError error) {
            return ResponseError.makeResponse(error);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, ServletRequest request, @PathVariable UUID id) {
        try {
            var foundTask = taskRepository.findById(id).orElse(null);
            var userId = (UUID) request.getAttribute("userId");
            if (foundTask == null || !foundTask.getUserId().equals(userId)) {
                throw new AppError(HttpStatus.FORBIDDEN, "Task not found");
            }

            var propertiesToIgnore = new String[]{"id", "userId", "createdAt"};
            Merge.mergeNonNullProperties(taskModel, foundTask, propertiesToIgnore);
            foundTask.setTitle(foundTask.getTitle().trim());

            validateTitle(foundTask.getTitle(), foundTask.getUserId(), id);
            validateDates(foundTask);

            return ResponseEntity.ok(taskRepository.save(foundTask));
        } catch (AppError error) {
            return ResponseError.makeResponse(error);
        }
    }


    /**
     * Checks whether the given title is valid.
     *
     * @param title      the new task title.
     * @param userId     the user id.
     * @param skidTaskId the task id to be skipped. Used when updating a given task.
     * @throws AppError indicating the error if it exists.
     */
    private void validateTitle(String title, UUID userId, UUID skidTaskId) throws AppError {
        if (title == null) throw new AppError("Title is required");

        title = title.trim();
        if (title.isEmpty()) throw new AppError("Title must not be empty.");

        if (title.length() > Config.taskTitleMaximumLength) {
            throw new AppError("Title must be at most 50 characters long.");
        }

        var foundTasksWithSameTitle = taskRepository.findByTitle(title);
        if (foundTasksWithSameTitle == null) return;

        for (var task : foundTasksWithSameTitle) {
            if (task.getUserId().equals(userId) && !task.getId().equals(skidTaskId)) {
                throw new AppError(HttpStatus.FORBIDDEN, "Title already exists.");
            }
        }
    }


    /**
     * Checks whether the given task dates are valid.
     *
     * @param task The task to be checked.
     * @throws AppError indicating the error if it exists.
     */

    private void validateDates(TaskModel task) throws AppError {
        if (task.getStartAt() == null || task.getEndAt() == null) {
            throw new AppError("Dates are required.");
        }

        if (!task.getStartAt().isAfter(LocalDateTime.now())) {
            throw new AppError("Start date should be greater than current date.");
        }

        if (!task.getEndAt().isAfter(task.getStartAt())) {
            throw new AppError("End date should be greater than start date.");
        }
    }
}
