package com.example.task.restproductapi.controller;

import com.example.task.restproductapi.Dto.JwtRequest;
import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.Security.JwtHelper;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;
import com.example.task.restproductapi.service.auth.AuthService;
import com.example.task.restproductapi.service.auth.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    MockMvc mockMvc;

    @Mock
    AuthService authService;

    @InjectMocks
    AuthController authController;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepo userRepo;

    @Mock
    JwtHelper helper;

    @Mock
    AuthenticationManager authenticationManager;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testCreateLoginToken_Success() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("user@example.com", "password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user@example.com", "password", new ArrayList<>());

        User user = new User();
        user.setName("John Doe");

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(helper.generateToken(userDetails)).thenReturn("mocked-jwt-token");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(any(Authentication.class));

        mockMvc.perform(post("/authorize/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Expose-Headers", "Authorization"))
                .andExpect(header().string("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header"))
                .andExpect(header().string("Authorization", "Bearer mocked-jwt-token"))
                .andExpect(jsonPath("$.jwtToken").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.username").value("John Doe"));
    }

    @Test
    void testCreateLoginToken_UserNotFound() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("user@example.com", "password");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user@example.com", "password", new ArrayList<>());

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userRepo.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(any(Authentication.class));

        mockMvc.perform(post("/authorize/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUser_UserAlreadyExists() throws Exception {
        RegisterReqDto registerReqDto = new RegisterReqDto();
        registerReqDto.setEmail("existing@example.com");
        registerReqDto.setPassword("password");

        when(authService.userExistsWithEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/authorize/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReqDto)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void testAddUser_Success() throws Exception {
        RegisterReqDto registerReqDto = new RegisterReqDto();
        registerReqDto.setEmail("newuser@example.com");
        registerReqDto.setPassword("password");

        UserDto userDto = new UserDto();
        userDto.setEmail("newuser@example.com");
        userDto.setName("New User");

        when(authService.userExistsWithEmail(anyString())).thenReturn(false);
        when(authService.addUser(any(RegisterReqDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/authorize/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void testAddUser_BadRequest() throws Exception {
        RegisterReqDto registerReqDto = new RegisterReqDto();
        registerReqDto.setEmail("newuser@example.com");
        registerReqDto.setPassword("password");

        when(authService.userExistsWithEmail(anyString())).thenReturn(false);
        when(authService.addUser(any(RegisterReqDto.class))).thenReturn(null);

        mockMvc.perform(post("/authorize/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReqDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCheckEmailExists_EmailExists() throws Exception {
        when(authService.userExistsWithEmail(anyString())).thenReturn(true);

        mockMvc.perform(get("/authorize/check-email")
                        .param("email", "existing@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCheckEmailExists_EmailDoesNotExist() throws Exception {
        when(authService.userExistsWithEmail(anyString())).thenReturn(false);

        mockMvc.perform(get("/authorize/check-email")
                        .param("email", "newuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

}