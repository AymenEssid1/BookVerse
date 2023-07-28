package com.aymen.security.controllers;


import com.aymen.security.auth.AuthenticationResponse;
import com.aymen.security.auth.AuthenticationService;
import com.aymen.security.auth.RegisterRequest;
import com.aymen.security.user.Role;
import com.aymen.security.user.User;
import com.aymen.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    public  AuthenticationService service;
    @Autowired
    public UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestParam Role role,
            @RequestBody RegisterRequest request
    ) {

        if (service.userExistsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(service.register(request,role));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserBy/{userId}")
    @PreAuthorize("hasAuthority('admin:read') OR hasAuthority('user:read')")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/updateUser/{userId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User updatedUser) {
        User existingUser = userService.getUserById(userId);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the email is being changed
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && service.userExistsByEmail(updatedUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        existingUser.setFirstname(updatedUser.getFirstname());
        existingUser.setLastname(updatedUser.getLastname());
        existingUser.setEmail(updatedUser.getEmail());

        // Only update the password if it is provided
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setRole(updatedUser.getRole());
        User savedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(savedUser);
    }



}
