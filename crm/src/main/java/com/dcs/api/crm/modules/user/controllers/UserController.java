package com.dcs.api.crm.modules.user.controllers;

import com.dcs.api.crm.configs.JwtUtil;
import com.dcs.api.crm.models.ApiError;
import com.dcs.api.crm.modules.user.dtos.UserLoginDto;
import com.dcs.api.crm.modules.user.dtos.UserRegistrationDto;
import com.dcs.api.crm.modules.user.resources.LoginResponse;
import com.dcs.api.crm.modules.user.services.CPOUserDetailsService;
import com.dcs.api.crm.schemas.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * The UserController class handles user authentication and registration requests.
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private CPOUserDetailsService userService;


    public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CPOUserDetailsService cpoUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = cpoUserDetailsService;
    }


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
            String email = authentication.getName();
            User user = User.builder().email(email).build();
            String token = jwtUtil.createToken(user);
            LoginResponse loginRes = new LoginResponse(email,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userDto) {
        log.info("Request parsed {}", userDto.toString());
        try {
            return ResponseEntity.ok(userService.registerUser(userDto));
        } catch (BadCredentialsException ex) {
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,"Something went wrong, couldn't register.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @GetMapping("user/error")
    public ResponseEntity<HttpStatus> accessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
