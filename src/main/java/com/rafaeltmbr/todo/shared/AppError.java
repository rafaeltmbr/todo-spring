package com.rafaeltmbr.todo.shared;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppError {
    private Integer status;
    private String message;

}
