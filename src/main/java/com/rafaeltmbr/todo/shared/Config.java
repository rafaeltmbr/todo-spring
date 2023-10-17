package com.rafaeltmbr.todo.shared;

import lombok.Data;

@Data
public final class Config {
    public static final Integer passwordHashCostRounds = 10;
    public static final String[] authenticatedPathsPrefix = {"/task"};
}
