package com.example.task.restproductapi.service.auth;

import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;

public interface AuthService {
    UserDto addUser(RegisterReqDto registerDto);
    boolean userExistsWithEmail(String email);


}
