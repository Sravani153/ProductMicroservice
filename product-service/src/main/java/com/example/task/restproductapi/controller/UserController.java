package com.example.task.restproductapi.controller;

import java.util.List;

import com.example.task.restproductapi.Dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id: ";

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            throw new NotFoundException("No users found.");
        } else {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id)  {
        UserDto userId = userService.getUserById(id);
        if (userId == null) {
            throw  new NotFoundException(USER_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(userId, HttpStatus.OK);
        }
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            this.userService.deleteUser(id);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (Exception e) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE + id);
        }
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        UserDto updateUser = this.userService.updateUser(id, user);
        if (updateUser == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        }
    }
}
