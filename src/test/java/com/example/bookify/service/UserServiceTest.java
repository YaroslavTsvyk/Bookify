package com.example.bookify.service;

import com.example.bookify.exception.ResourceNotFoundException;
import com.example.bookify.model.Role;
import com.example.bookify.model.User;
import com.example.bookify.repository.UserRepository;
import com.example.bookify.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @InjectMocks private UserServiceImpl userService;

    // Test data
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Reed")
                .email("jane@example.com")
                .password("sweetjane")
                .build();
    }

    @Test
    void create_shouldSaveAndReturnEntity() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertThat(result).isEqualTo(user);

        verify(userRepository).save(user);
    }

    @Test
    void getById_shouldReturnEntity() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.getById(user.getId());

        assertThat(result).isEqualTo(user);

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getById_shouldThrowIfUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(user.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id " + user.getId() + " not found");
    }

    @Test
    void getAll_shouldReturnListOfEntities() {
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertThat(result).isEqualTo(users);

        verify(userRepository).findAll();
    }

    @Test
    void update_shouldUpdateAndReturnEntity() {
        User updatedUser = User.builder()
                .firstName("New")
                .lastName("Surname")
                .email("new@example.com")
                .password("newPass")
                .role(Role.ADMIN)
                .build();

        User savedUser = User.builder()
                .id(user.getId())
                .firstName("New")
                .lastName("Surname")
                .email("new@example.com")
                .password("newPass")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.update(user.getId(), updatedUser);

        assertThat(result).isEqualTo(savedUser);

        assertThat(user.getFirstName()).isEqualTo("New");
        assertThat(user.getLastName()).isEqualTo("Surname");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("newPass");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void update_shouldThrowIfUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(user.getId(), user))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id " + user.getId() + " not found");

        verify(userRepository).findById(user.getId());
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteIfUserFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepository).findById(user.getId());
        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrowIfUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(user.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id " + user.getId() + " not found");

        verify(userRepository).findById(user.getId());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(user.getEmail());

        assertThat(result.getUsername()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsername_shouldThrowIfUserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with username " + user.getEmail() + " not found");

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getCurrentUser_shouldReturnUserFromContext() {
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertThat(result).isEqualTo(user);
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getCurrentUser_shouldThrowIfPrincipalNotUserDetails() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("justString");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Authentication error");
    }

    @Test
    void getCurrentUser_shouldThrowIfUserNotFoundInRepository() {
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of()
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with username " + user.getEmail() + " not found");
    }
}
