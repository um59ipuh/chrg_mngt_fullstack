package com.dcs.api.crm.modules.user.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dcs.api.crm.configs.JwtUtil;
import com.dcs.api.crm.modules.user.dtos.UserLoginDto;
import com.dcs.api.crm.modules.user.dtos.UserRegistrationDto;
import com.dcs.api.crm.modules.user.services.CPOUserDetailsService;
import com.dcs.api.crm.schemas.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CPOUserDetailsService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        String token = "dummyToken";

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(authentication);
        given(jwtUtil.createToken(any(User.class))).willReturn(token);

        // Act & Assert
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("invalid@example.com");
        userLoginDto.setPassword("wrongpassword");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setFirstname("First");
        userDto.setLastname("Last");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        User user = User.builder()
                .firstName(userDto.getFirstname())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password("encodedPassword")
                .build();

        given(userService.registerUser(any(UserRegistrationDto.class))).willReturn(user);

        // Act & Assert
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws Exception {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setFirstname("First");
        userDto.setLastname("Last");
        userDto.setEmail("existing@example.com");
        userDto.setPassword("password");

        given(userService.registerUser(any(UserRegistrationDto.class))).willThrow(new BadCredentialsException("Something went wrong, couldn't register."));

        // Act & Assert
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Something went wrong, couldn't register."))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }
}
