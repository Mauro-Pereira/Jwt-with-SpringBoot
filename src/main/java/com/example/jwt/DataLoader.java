package com.example.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setName("root");
        user.setEmail("root@email.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setAdmin(true);

        userRepository.save(user);
    }
}
