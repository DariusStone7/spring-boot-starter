package com.spring_boot_starter.api.modules.users.controller;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.core.service.UserService;
import com.spring_boot_starter.api.modules.users.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public Page<User> findAll(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        return userService.findAll(page, size, sortBy, sortDir, username, email, status);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable(required = true) Long id) {
        return userService.findOne(id);
    }

    @PostMapping("")
    public ResponseEntity<String> add(@Valid @RequestBody() UserDto userDto) {
        return userService.add(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable(required = true) Long id, @Valid @RequestBody() UserDto userDto) {
        return userService.update(id, userDto);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> enable(@PathVariable(required = true) Long id) {
        return userService.enable(id);
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<String> disable(@PathVariable(required = true) Long id) {
        return userService.disable(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(required = true) Long id) {
        return userService.delete(id);
    }

}
