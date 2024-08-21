package com.example.jwt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt.entity.User;
import com.example.jwt.exception.UserAlreadyExistsException;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        Optional<User> optionalUser = this.userRepository.findByEmail(user.getEmail());
        User newUser = optionalUser.get();

        if(newUser.getId() != null){
            throw new UserAlreadyExistsException("This user is Already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        Optional<User> newUser = userRepository.findById(id);

        if(newUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        newUser.get().setName(user.getName());
        newUser.get().setEmail(user.getEmail());
        newUser.get().setPassword(user.getPassword());

        return userRepository.save(newUser.get());
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);

        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    public String setAdminUser(Long id){

        Optional<User> newUser = userRepository.findById(id);

        if (newUser.isEmpty()) {
            return "User not found";       
        }

        if(newUser.get().isAdmin()){
            return "This user is already an admin";
        }

        newUser.get().setAdmin(true);

        return "User is an admin now";

    }

    public String removeAdminUser(Long id){
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return "User not found";       
        }

        if(!optionalUser.get().isAdmin()){
            return "This user is not an admin";
        }

        optionalUser.get().setAdmin(false);

        return "This user is a common user now";



    }
    
}
