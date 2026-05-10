package com.spring_boot_starter.api.modules.users.service;

import com.spring_boot_starter.api.core.service.UserService;
import com.spring_boot_starter.api.core.entity.User;
import com.spring_boot_starter.api.core.enums.UserStatus;
import com.spring_boot_starter.api.exceptions.ResourceAlreadyExistsException;
import com.spring_boot_starter.api.exceptions.ResourceNotFoundException;
import com.spring_boot_starter.api.modules.users.dto.UserDto;
import com.spring_boot_starter.api.modules.users.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> findAll(int page, int size, String sortBy, String sortDir, String username, String email, String status) {
        log.info("Find all users attempt");
        Sort sort = ("desc").equals(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(username != null && !username.isEmpty()){
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if(email != null && !email.isEmpty()){
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if(status != null && !status.isEmpty()){
                predicates.add(cb.like(cb.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
            }

            return  cb.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User findOne(Long id) {
        log.info("Find one user attempt");
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public ResponseEntity<String> add(UserDto userDto) {
        try {
            log.info("Add user attempt");
            User user = userRepository.findByUsernameOrEmail(userDto.username(), userDto.email());
            if (user != null) {
                throw new ResourceAlreadyExistsException("User already exist");
            }
            user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setPassword(passwordEncoder.encode(userDto.password()));
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            System.out.println("Add user error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> update(Long id, UserDto userDto) throws ResourceNotFoundException {
        log.info("Update user attempt");
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

        user.setUsername(userDto.username());
        user.setProfiles(userDto.profiles());
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully");
    }

    @Override
    public ResponseEntity<String> enable(Long id) {
        log.info("Enable user attempt");
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return ResponseEntity.ok("User enabled successfully");
    }

    @Override
    public ResponseEntity<String> disable(Long id) {
        log.info("Disable user attempt");
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

        return ResponseEntity.ok("User disabled successfully");
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        log.info("Delete user attempt");
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
