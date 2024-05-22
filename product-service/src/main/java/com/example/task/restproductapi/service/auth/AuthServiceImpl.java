package com.example.task.restproductapi.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepo userRepo;

    @Override
    public UserDto addUser(RegisterReqDto registerDto) {
        User user=new User();

        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setRole(registerDto.getUserRole());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
       User createdUser= userRepo.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setName(createdUser.getName());
        userDto.setEmail(createdUser.getEmail());
        userDto.setPassword(createdUser.getPassword());
        return userDto;
    }

    @Override
    public boolean userExistsWithEmail(String email) {
        Optional<User> existingUser = userRepo.findUserByEmail(email);
        return existingUser.isPresent();
    }

}
