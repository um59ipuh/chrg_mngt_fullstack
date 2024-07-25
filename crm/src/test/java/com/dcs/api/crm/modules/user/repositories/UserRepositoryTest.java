package com.dcs.api.crm.modules.user.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.dcs.api.crm.schemas.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void testFindUserByEmail() {
        // Set up
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        given(userRepository.findUserByEmail(email)).willReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userRepository.findUserByEmail(email);

        // Assert
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void testFindUserByEmail_NotFound() {
        // Arrange
        String email = "notfound@example.com";

        given(userRepository.findUserByEmail(email)).willReturn(null);

        // Act
        Optional<User> foundUser = userRepository.findUserByEmail(email);

        // Assert
        assertThat(foundUser).isNull();
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setUserID(1);
        user.setEmail("save@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        given(userRepository.save(user)).willReturn(user);

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserID()).isEqualTo(1);
        assertThat(savedUser.getEmail()).isEqualTo("save@example.com");
        assertThat(savedUser.getFirstName()).isEqualTo("Test");
        assertThat(savedUser.getLastName()).isEqualTo("User");

        // Verify that the save method was called
        verify(userRepository).save(user);
    }
}
