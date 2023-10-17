package com.rafaeltmbr.todo.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rafaeltmbr.todo.error.AppError;
import com.rafaeltmbr.todo.shared.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserRepository repository;

    @GetMapping
    public List<UserModel> list() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel) throws AppError {
        var foundUser = repository.findByUsername(userModel.getUsername());
        if (foundUser != null) throw new AppError(HttpStatus.FORBIDDEN, "Username already exists.");

        var passwordHash = BCrypt
                .withDefaults()
                .hashToString(Config.passwordHashCostRounds, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHash);

        var createdUser = repository.save(userModel);
        return ResponseEntity.ok(createdUser);
    }
}
