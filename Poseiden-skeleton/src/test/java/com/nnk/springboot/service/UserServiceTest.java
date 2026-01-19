package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void create_shouldEncodePassword_andSave() {
        User u = new User();
        u.setUsername("john");
        u.setPassword("plain");
        u.setFullname("John Doe");
        u.setRole("USER");

        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.create(u);

        assertEquals("hashed", saved.getPassword());
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(u);
    }

    @Test
    void update_shouldEncodePassword_andSave_withId() {
        User u = new User();
        u.setPassword("plain2");

        when(passwordEncoder.encode("plain2")).thenReturn("hashed2");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.update(5, u);

        assertEquals(5, updated.getId());
        assertEquals("hashed2", updated.getPassword());
        verify(passwordEncoder).encode("plain2");
        verify(userRepository).save(u);
    }

    @Test
    void delete_shouldFindAndDelete() {
        User u = new User();
        u.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(u));

        userService.delete(1);

        verify(userRepository).delete(u);
    }

    @Test
    void findAll_shouldReturnUsers() {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("jane");

        when(userRepository.findAll()).thenReturn(java.util.List.of(u1, u2));

        java.util.List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("john", users.get(0).getUsername());
        assertEquals("jane", users.get(1).getUsername());

        verify(userRepository).findAll();
        verifyNoInteractions(passwordEncoder); // findAll ne doit pas encoder
    }
}
