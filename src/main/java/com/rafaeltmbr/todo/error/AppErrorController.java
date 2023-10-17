package com.rafaeltmbr.todo.error;


import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppErrorController {

    @ExceptionHandler(AppError.class)
    public ResponseEntity<ResponseErrorMessage> handleAppError(AppError appError) {
        return ResponseEntity
                .status(appError.getStatus())
                .body(new ResponseErrorMessage(appError));
    }

    @Data
    public static class ResponseErrorMessage {
        private Integer status;
        private String message;

        ResponseErrorMessage(AppError error) {
            this.status = error.getStatus();
            this.message = error.getMessage();
        }
    }
}
