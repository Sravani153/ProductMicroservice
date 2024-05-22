package com.example.task.restproductapi.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.task.restproductapi.Dto.UserDto;
import org.springframework.stereotype.Service;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo repo;

    private static final String USER_NOT_FOUND_IN_DB = "No User Found for this Id in Database ";

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repo.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repo.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_IN_DB));
        return convertToDto(user);
    }


    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        UserDto existingUserDto = getUserById(userId);
        User existingUser = convertToEntity(existingUserDto);
        User user = convertToEntity(userDto);

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());

        // Save the updated user entity
        User updatedUser = repo.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }
  
}
