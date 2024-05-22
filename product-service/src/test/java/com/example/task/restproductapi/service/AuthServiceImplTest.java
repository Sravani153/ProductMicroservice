package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.Enum.UserRole;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;
import com.example.task.restproductapi.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    UserRepo userRepo;

    @InjectMocks
    AuthServiceImpl authService;
    RegisterReqDto registerReqDto;
    User user;
    @BeforeEach
    void setUp() {
        registerReqDto = new RegisterReqDto();
        registerReqDto.setName("sravani");
        registerReqDto.setEmail("sravani@gmail.com");
        registerReqDto.setPassword("sravs");
        registerReqDto.setUserRole(UserRole.ADMIN);

        user = new User();
        user.setId(1L);
        user.setName(registerReqDto.getName());
        user.setEmail(registerReqDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerReqDto.getPassword()));
    }

    @Test
    void testAddUser_ShouldReturnUserDto() {
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDto userDto = authService.addUser(registerReqDto);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    void testUserExistsWithEmail_ShouldReturnTrue() {
        String email = "sravani@gmail.com";
        when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(new User()));

        assertTrue(authService.userExistsWithEmail(email));
    }

    @Test
    void testWhenUserDoesNotExistWithEmail_ShouldReturnFalse() {
        String email = "nonexistent@example.com";
        when(userRepo.findUserByEmail(email)).thenReturn(Optional.empty());

        assertFalse(authService.userExistsWithEmail(email));
    }
}

