package com.nnk.springboot.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Used to change the password of admin user
 */
@Component
public class PasswordHashPrinter implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public PasswordHashPrinter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String raw = "Admin1";
        System.out.println("RAW=" + raw);
        System.out.println("BCRYPT=" + passwordEncoder.encode(raw));
    }
}
