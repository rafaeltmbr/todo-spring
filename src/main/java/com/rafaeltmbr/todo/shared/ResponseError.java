package com.rafaeltmbr.todo.shared;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseError {

    public static ResponseEntity<ErrorMessage> makeResponse(AppError error) {
        return ResponseEntity.status(error.getStatus()).body(new ErrorMessage(error));
    }

    public static ResponseEntity<ErrorMessage> makeResponse(HttpStatus status, String message) {
        return makeResponse(new AppError(status.value(), message));
    }

    @Data
    public static class ErrorMessage {
        private Integer status;
        private String message;

        public ErrorMessage(AppError error) {
            this.status = error.getStatus();
            this.message = error.getMessage();
        }
    }
}
