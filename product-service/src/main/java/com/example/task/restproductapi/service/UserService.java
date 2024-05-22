package com.example.task.restproductapi.service;

import java.util.List;

import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.entities.User;

public interface UserService {
 
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);
    UserDto updateUser(Long userId, UserDto user);
 
    void deleteUser(Long  id);

    
}
