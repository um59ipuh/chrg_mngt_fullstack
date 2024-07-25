package com.dcs.api.crm.modules.user.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.dcs.api.crm.exceptions.BadRequestException;
import com.dcs.api.crm.exceptions.NotFoundException;
import com.dcs.api.crm.modules.user.dtos.UserRegistrationDto;
import com.dcs.api.crm.modules.user.repositories.UserRepository;
import com.dcs.api.crm.schemas.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CPOUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CPOUserDetailsService cpoUserDetailsService;


    @AfterEach
    void tearDown() {
        reset(userRepository, passwordEncoder);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");

        given(userRepository.findUserByEmail(email)).willReturn(Optional.of(user));

        // Act
        UserDetails userDetails = cpoUserDetailsService.loadUserByUsername(email);

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("password");

        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "notfound@example.com";

        given(userRepository.findUserByEmail(email)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cpoUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User Not Found with email id " + email);

        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setFirstname("First");
        userDto.setLastname("Last");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        given(userRepository.findUserByEmail(userDto.getEmail())).willReturn(Optional.of(new User()));

        // Act & Assert
        assertThatThrownBy(() -> cpoUserDetailsService.registerUser(userDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("User Already Exist with email " + userDto.getEmail());

        verify(userRepository).findUserByEmail(userDto.getEmail());
    }

    @Test
    void testRegisterUser_Success() {
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

        given(userRepository.findUserByEmail(userDto.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(userDto.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(user)).willReturn(user);

        User registeredUser = cpoUserDetailsService.registerUser(userDto);

        // Assert
        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).findUserByEmail(userDto.getEmail());
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(userRepository).save(user);
    }
}