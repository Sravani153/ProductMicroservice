package com.example.task.restproductapi.service.auth;

import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.Enum.UserRole;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterReqDto registerReqDto;
    private User user;

    @BeforeEach
    void setUp() {
        registerReqDto = new RegisterReqDto();
        registerReqDto.setName("Test User");
        registerReqDto.setEmail("test@example.com");
        registerReqDto.setPassword("password");
        registerReqDto.setUserRole(UserRole.USER);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setRole(UserRole.USER);
    }

    @Test
    void testAddUser_Success() {
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDto result = authService.addUser(registerReqDto);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertNotEquals("password", result.getPassword());  // Ensure password is encrypted
    }

    @Test
    void testUserExistsWithEmail_UserExists() {
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        boolean exists = authService.userExistsWithEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    void testUserExistsWithEmail_UserDoesNotExist() {
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.empty());

        boolean exists = authService.userExistsWithEmail("test@example.com");

        assertFalse(exists);
    }
}
