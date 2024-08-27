package com.example.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.jwt.entity.Role;
import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;

/*
 * This class creates an admin user on the system
*/

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
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }
}
