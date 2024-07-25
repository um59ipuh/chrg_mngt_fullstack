package com.dcs.api.crm.modules.user.services;

import com.dcs.api.crm.exceptions.BadRequestException;
import com.dcs.api.crm.exceptions.NotFoundException;
import com.dcs.api.crm.modules.user.dtos.UserRegistrationDto;
import com.dcs.api.crm.modules.user.repositories.UserRepository;
import com.dcs.api.crm.schemas.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CPOUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public PasswordEncoder passwordEncoder;
    @Autowired
    public CPOUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Loads the user details by email.
     *
     * @param String email the email of the user
     * @return UserDetails containing user information
     * @throws UsernameNotFoundException if the user with the specified email is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User cpoUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found with email id " + email));

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(cpoUser.getEmail())
                        .password(cpoUser.getPassword())
                        .build();
        return userDetails;
    }


    /**
     * Registers a new user with the provided UserRegistrationDto.
     *
     * @param UserRegistrationDto userDto the DTO containing user registration information
     * @return User the registered User
     * @throws BadCredentialsException if a user with the specified email already exists
     */
    public User registerUser(UserRegistrationDto userDto) {

        if (userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new BadCredentialsException("User Already Exist with email " + userDto.getEmail());
        }

        User user = User.builder().firstName(userDto.getFirstname())
                                .lastName(userDto.getLastname())
                                .email(userDto.getEmail())
                                        .password(passwordEncoder.encode(userDto.getPassword())).build();

        log.info("User generated - {}", user.toString());
        return userRepository.save(user);
    }
}
