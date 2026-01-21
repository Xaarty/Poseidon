package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUnknownUser() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void loadCheckPrefixIncorrect() {
        User u = new User();
        u.setUsername("mika");
        u.setPassword("hashed");
        u.setRole("ADMIN");

        when(userRepository.findByUsername("mika"))
                .thenReturn(Optional.of(u));

        UserDetails details = customUserDetailsService.loadUserByUsername("mika");

        assertEquals("mika", details.getUsername());
        assertEquals("hashed", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadPrefixCorrect() {
        User u = new User();
        u.setUsername("john");
        u.setPassword("hashed2");
        u.setRole("ROLE_USER");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(u));

        UserDetails details = customUserDetailsService.loadUserByUsername("john");

        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}
