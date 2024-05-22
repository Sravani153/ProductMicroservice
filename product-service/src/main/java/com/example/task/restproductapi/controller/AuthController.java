package com.example.task.restproductapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.task.restproductapi.Dto.JwtRequest;
import com.example.task.restproductapi.Dto.JwtResponse;
import com.example.task.restproductapi.Dto.RegisterReqDto;
import com.example.task.restproductapi.Dto.UserDto;
import com.example.task.restproductapi.Security.JwtHelper;
import com.example.task.restproductapi.service.auth.AuthService;
import com.example.task.restproductapi.service.auth.UserDetailsServiceImpl;
import com.example.task.restproductapi.entities.User;
import com.example.task.restproductapi.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
@CrossOrigin(origins = ("*"), maxAge = 3600)
public class AuthController {
    private final UserDetailsServiceImpl userDetailsService;
    private  final AuthenticationManager manager;
    private final JwtHelper helper;
    private final AuthService authService;
    private final UserRepo userRepo;
    public static final String TOKEN_PREFIX="Bearer ";
    public  static final String HEADER_STRING="Authorization";
    
    @PostMapping("/login")
public JwtResponse createLoginToken(@RequestBody JwtRequest request, HttpServletResponse response) throws IOException {

    this.doAuthenticate(request.getEmail(), request.getPassword());

    final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    Optional<User> user = userRepo.findUserByEmail(userDetails.getUsername());
    final String token = helper.generateToken(userDetails); // Pass UserDetails object here


        if (user.isPresent()) {
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, " +
                "X-Requested-With, Content-Type, Accept, X-Custom-header");
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(user.get().getName()) 
                .build();

        return jwtResponse;
    }

    return null; 
}

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@RequestBody RegisterReqDto registerDto) {
        if (authService.userExistsWithEmail(registerDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    
        UserDto user = authService.addUser(registerDto);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-email")
    public boolean checkEmailExists(@RequestParam String email) {
        return authService.userExistsWithEmail(email);
    }
}
