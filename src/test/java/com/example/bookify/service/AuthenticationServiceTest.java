package com.example.bookify.service;

import com.example.bookify.dto.AuthenticationRequest;
import com.example.bookify.dto.AuthenticationResponse;
import com.example.bookify.dto.RegisterRequest;
import com.example.bookify.model.Role;
import com.example.bookify.model.User;
import com.example.bookify.repository.UserRepository;
import com.example.bookify.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("John", "Doe", "john@example.com", "password123");
        authRequest = new AuthenticationRequest("john@example.com", "password123");

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("encodedPass");
        user.setRole(Role.USER);
    }

    @Test
    void register_shouldSaveUserAndReturnToken() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void testRegister_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password");

        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        assertThatThrownBy(() -> authenticationService.register(request))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void authenticate_shouldAuthenticateAndReturnToken() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("john@example.com", "password123")
        );
        verify(userRepository).findByEmail("john@example.com");
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticate_shouldThrowException_whenBadCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("wrong@example.com", "badpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void testAuthenticate_shouldThrowException_whenUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("notfound@example.com", "pass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
