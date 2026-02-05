package com.nnk.springboot.service;


import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Récupération de tous les User
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Récupération d'un User par identifiant unique
     */
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invalid user Id:" + id));
    }

    /**
     * Création d'un User (avec encodage du mot de passe)
     */
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    /**
     * Modification d'un User par identifiant unique (avec encodage du mot de passe)
     */
    public User update(Integer id, User user) {
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Suppression d'un User par identifiant unique
     */
    public void delete(Integer id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}