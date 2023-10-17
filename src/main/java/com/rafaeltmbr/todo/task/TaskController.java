package com.rafaeltmbr.todo.task;


import com.rafaeltmbr.todo.shared.AppError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;

    @GetMapping
    public List<TaskModel> list() {
        return taskRepository.findAll();
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var foundTask = taskRepository.findByTitle(taskModel.getTitle());
        if (foundTask != null) {
            var err = AppError.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .message("Title already exists.")
                    .build();

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
        }

        System.out.println(request.getAttribute("userId"));

        taskModel.setUserId((UUID) request.getAttribute("userId"));
        var newTask = taskRepository.save(taskModel);
        return ResponseEntity.ok(newTask);
    }
}
