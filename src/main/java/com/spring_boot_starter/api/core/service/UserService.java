package com.spring_boot_starter.api.core.service;

import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.modules.users.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public Page<User> findAll(int page, int size, String sortBy, String sortDir, String username, String email, String status);

    public User findOne(Long id);

    public ResponseEntity<String> add(UserDto userDto);

    public ResponseEntity<String> update(Long id, UserDto userDto);

    public ResponseEntity<String> delete(Long id);

    public ResponseEntity<String> enable(Long id);

    public ResponseEntity<String> disable(Long id);
}
