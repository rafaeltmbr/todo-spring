package com.rafaeltmbr.todo.shared;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppError extends Exception {
    private Integer status;

    public AppError(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public AppError(HttpStatus status, String message) {
        this(status.value(), message);
    }

    public AppError(String message) {
        this(400, message);
    }
}
