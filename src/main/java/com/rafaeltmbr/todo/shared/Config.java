package com.rafaeltmbr.todo.shared;

import lombok.Data;

@Data
public final class Config {
    public static final int passwordHashCostRounds = 10;
    public static final String[] authenticatedPathsPrefix = {"/task"};
    public static final int taskTitleMaximumLength = 50;
}
