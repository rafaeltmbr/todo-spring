package com.rafaeltmbr.todo.user;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;

@RestController
@RequestMapping("/user")
public class UserController {
    private final ArrayList<UserModel> users = new ArrayList<>();
    private final HashSet<String> usernames = new HashSet<>();

    @GetMapping
    public ArrayList<UserModel> list() {
        return users;
    }

    @PostMapping
    public UserModel create(@RequestBody UserModel userModel) {
        if (usernames.contains(userModel.getName())) throw new IllegalArgumentException("Username already exists.");

        users.add(userModel);
        usernames.add(userModel.getName());

        return userModel;
    }
}
